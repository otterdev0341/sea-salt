package com.otterdev.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.otterdev.domain.entity.ContactType;
import com.otterdev.error_structure.RepositoryError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ContactTypeRepository implements PanacheRepository<ContactType> {
    
    public Uni<Optional<ContactType>> findByIdAndUserId(UUID contactTypeId, UUID userId) {
        return find("FROM ContactType ct WHERE ct.id = ?1 AND ct.createdBy.id = ?2", contactTypeId, userId)
            .firstResult()
            .map(Optional::ofNullable);
    }

    public Uni<Boolean> isExistByDetailAndUserId(String detail, UUID userId) {
        return count("detail = ?1 AND createdBy.id = ?2", detail, userId)
            .map(count -> count > 0);
    }

    public Uni<Either<RepositoryError, Boolean>> deleteContactType(UUID contactTypeId, UUID userId) {
        return findByIdAndUserId(contactTypeId, userId)
                .onItem()
                .transformToUni(contactTypeOpt -> {
                    if (contactTypeOpt.isEmpty()) {
                        return Uni.createFrom().item(Either.left(new RepositoryError.NotFound("Contact type not found")));
                    }
                    return delete(contactTypeOpt.get())
                            .replaceWith(Either.right(true));
                });
    }

    public Uni<Either<RepositoryError, List<ContactType>>> findAllByUserId(UUID userId) {
        return find("""
                FROM ContactType ct 
                LEFT JOIN FETCH ct.createdBy u
                LEFT JOIN FETCH u.gender g
                LEFT JOIN FETCH u.role r
                WHERE ct.createdBy.id = ?1
                """, userId)
            .list()
            .onItem()
            .transform(contactTypes -> Either.<RepositoryError, List<ContactType>>right(contactTypes))
            .onFailure()
            .recoverWithItem(e -> Either.<RepositoryError, List<ContactType>>left(
                new RepositoryError.FetchFailed("Failed to retrieve contact types: " + e.getMessage())
            ));
    }

    public Uni<Either<RepositoryError, ContactType>> updateContactType(ContactType contactType, UUID userId) {
        String updateQuery = """
            UPDATE ContactType ct 
            SET ct.detail = ?1, ct.updatedAt = ?2 
            WHERE ct.id = ?3 AND ct.createdBy.id = ?4
            """;
        
        return update(updateQuery, 
                contactType.getDetail().trim(),
                LocalDateTime.now(),
                contactType.getId(),
                userId)
            .chain(updatedCount -> {
                if (updatedCount == 0) {
                    return Uni.createFrom().item(
                        Either.left(new RepositoryError.NotFound("Contact type not found or not updated"))
                    );
                }
                return find("""
                        FROM ContactType ct
                        LEFT JOIN FETCH ct.createdBy u
                        LEFT JOIN FETCH u.gender g
                        LEFT JOIN FETCH u.role r
                        WHERE ct.id = ?1 AND ct.createdBy.id = ?2
                        """, contactType.getId(), userId)
                    .firstResult()
                    .map(updatedContactType -> 
                        updatedContactType != null
                        ? Either.<RepositoryError, ContactType>right(updatedContactType)
                        : Either.<RepositoryError, ContactType>left(new RepositoryError.NotFound("Contact type not found or not updated"))
                    );
            });
    }

}
