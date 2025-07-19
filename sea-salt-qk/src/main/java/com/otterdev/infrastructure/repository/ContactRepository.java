package com.otterdev.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.otterdev.domain.entity.Contact;
import com.otterdev.domain.entity.ContactType;
import com.otterdev.error_structure.RepositoryError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ContactRepository implements PanacheRepository<Contact> {
    
    public Uni<Optional<Contact>> findByIdAndUserId(UUID contactId, UUID userId) {
        return find("FROM Contact c WHERE c.id = ?1 AND c.createdBy.id = ?2", contactId, userId)
            .firstResult()
            .map(Optional::ofNullable);
    }


    public Uni<Either<RepositoryError, Boolean>> deleteContact(UUID contactId, UUID userId) {
        return findByIdAndUserId(contactId, userId)
                .onItem()
                .transformToUni(contactOpt -> {
                    if (contactOpt.isEmpty()) {
                        return Uni.createFrom().item(Either.left(new RepositoryError.NotFound("Contact not found")));
                    }
                    return delete(contactOpt.get())
                            .replaceWith(Either.right(true));
                });
    }


    public Uni<Either<RepositoryError, List<Contact>>> findAllByUserId(UUID userId) {
        return find("""
                FROM Contact c 
                LEFT JOIN FETCH c.createdBy u
                LEFT JOIN FETCH u.gender g
                LEFT JOIN FETCH u.role r
                WHERE c.createdBy.id = ?1
                """, userId)
            .list()
            .onItem()
            .transform(contacts -> Either.<RepositoryError, List<Contact>>right(contacts))
            .onFailure()
            .recoverWithItem(e -> Either.<RepositoryError, List<Contact>>left(
                new RepositoryError.FetchFailed("Failed to retrieve contacts: " + e.getMessage())
            ));
    }

    public Uni<Either<RepositoryError, Contact>> updateContact(Contact contact, UUID userId) {
        
        String updateQuery = """
            UPDATE Contact c
            SET c.businessName = ?1, c.internalName = ?2, c.detail = ?3, c.note = ?4,
                c.contactType = ?5, c.address = ?6, c.phone = ?7, c.mobilePhone = ?8,
                c.line = ?9, c.email = ?10, c.updatedAt = ?11
            WHERE c.id = ?12 AND c.createdBy.id = ?13
            """;

        return update(updateQuery,
                contact.getBusinessName().trim(),
                contact.getInternalName(),
                contact.getDetail(),
                contact.getNote(),
                contact.getContactType(),
                contact.getAddress(),
                contact.getPhone(),
                contact.getMobilePhone(),
                contact.getLine(),
                contact.getEmail(),
                LocalDateTime.now(),
                contact.getId(),
                userId)
            .chain(updatedCount -> {
                if (updatedCount == 0) {
                    return Uni.createFrom().item(
                        Either.left(new RepositoryError.NotFound("Contact not found or not updated"))
                    );
                }
                return find("""
                        FROM Contact c
                        LEFT JOIN FETCH c.contactType ct
                        LEFT JOIN FETCH c.createdBy u
                        LEFT JOIN FETCH u.gender g
                        LEFT JOIN FETCH u.role r
                        WHERE c.id = ?1 AND c.createdBy.id = ?2
                        """, contact.getId(), userId)
                    .firstResult()
                    .map(updatedContact ->
                        updatedContact != null
                            ? Either.<RepositoryError, Contact>right(updatedContact)
                            : Either.<RepositoryError, Contact>left(new RepositoryError.NotFound("Contact not found or not updated"))
                    );
            });
    }


    public Uni<Either<RepositoryError, List<Contact>>> findByContactTypeAndUserId(ContactType contactType, UUID userId) {
        String query = """
            FROM Contact c
            LEFT JOIN FETCH c.contactType ct
            LEFT JOIN FETCH c.createdBy u
            LEFT JOIN FETCH u.gender g
            LEFT JOIN FETCH u.role r
            WHERE ct.id = ?1 AND u.id = ?2
            """;

        return find(query, contactType.getId(), userId)
            .list()
            .map(contacts -> {
                if (contacts == null || contacts.isEmpty()) {
                    return Either.left(new RepositoryError.NotFound(
                        "No contacts found for the given contact type and user."
                    ));
                }
                return Either.<RepositoryError, List<Contact>>right(contacts);
            });
    }

}
