package com.otterdev.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.otterdev.domain.entity.PropertyStatus;
import com.otterdev.error_structure.RepositoryError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PropertyStatusRepository implements PanacheRepository<PropertyStatus> {
    
    public Uni<Optional<PropertyStatus>> findByIdAndUserId(UUID propertyStatusId, UUID userId) {
        return find("FROM PropertyStatus ps WHERE ps.id = ?1 AND ps.createdBy.id = ?2", propertyStatusId, userId)
            .firstResult()
            .map(Optional::ofNullable);
    }

    public Uni<Boolean> isExistByDetailAndUserId(String detail, UUID userId) {
        return count("detail = ?1 AND createdBy.id = ?2", detail, userId)
            .map(count -> count > 0);
    }

    public Uni<Either<RepositoryError, Boolean>> deletePropertyStatus(UUID propertyStatusId, UUID userId) {
        return findByIdAndUserId(propertyStatusId, userId)
                .onItem()
                .transformToUni(propertyStatusOpt -> {
                    if (propertyStatusOpt.isEmpty()) {
                        return Uni.createFrom().item(Either.left(new RepositoryError.NotFound("Property status not found")));
                    }
                    return delete(propertyStatusOpt.get())
                            .replaceWith(Either.right(true));
                });
    }

    public Uni<Either<RepositoryError, List<PropertyStatus>>> findAllByUserId(UUID userId) {
        return find("""
                FROM PropertyStatus ps 
                LEFT JOIN FETCH ps.createdBy u
                LEFT JOIN FETCH u.gender g
                LEFT JOIN FETCH u.role r
                WHERE ps.createdBy.id = ?1
                """, userId)
            .list()
            .onItem()
            .transform(propertyStatuses -> Either.<RepositoryError, List<PropertyStatus>>right(propertyStatuses))
            .onFailure()
            .recoverWithItem(e -> Either.<RepositoryError, List<PropertyStatus>>left(
                new RepositoryError.FetchFailed("Failed to retrieve property statuses: " + e.getMessage())
            ));
    }

    public Uni<Either<RepositoryError, PropertyStatus>> updatePropertyStatus(PropertyStatus propertyStatus, UUID userId) {
        String updateQuery = """
            UPDATE PropertyStatus ps 
            SET ps.detail = ?1, ps.updatedAt = ?2 
            WHERE ps.id = ?3 AND ps.createdBy.id = ?4
            """;
        
        return update(updateQuery, 
                propertyStatus.getDetail().trim(),
                LocalDateTime.now(),
                propertyStatus.getId(),
                userId)
            .chain(updatedCount -> {
                if (updatedCount == 0) {
                    return Uni.createFrom().item(
                        Either.left(new RepositoryError.NotFound("Property status not found or not updated"))
                    );
                }
                return find("""
                        FROM PropertyStatus ps
                        LEFT JOIN FETCH ps.createdBy u
                        LEFT JOIN FETCH u.gender g
                        LEFT JOIN FETCH u.role r
                        WHERE ps.id = ?1 AND ps.createdBy.id = ?2
                        """, propertyStatus.getId(), userId)
                    .firstResult()
                    .map(updatedPropertyStatus -> 
                        updatedPropertyStatus != null
                        ? Either.<RepositoryError, PropertyStatus>right(updatedPropertyStatus)
                        : Either.<RepositoryError, PropertyStatus>left(new RepositoryError.NotFound("Property status not found or not updated"))
                    );
            });
    }
}
