package com.otterdev.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.otterdev.domain.entity.PropertyType;
import com.otterdev.error_structure.RepositoryError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PropertyTypeRepository implements PanacheRepository<PropertyType> {
    
    public Uni<Optional<PropertyType>> findByIdAndUserId(UUID propertyTypeId, UUID userId) {
        return find("FROM PropertyType pt WHERE pt.id = ?1 AND pt.createdBy.id = ?2", propertyTypeId, userId)
            .firstResult()
            .map(Optional::ofNullable);
    }

    public Uni<Boolean> isExistByDetailAndUserId(String detail, UUID userId) {
        return count("detail = ?1 AND createdBy.id = ?2", detail, userId)
            .map(count -> count > 0);
    }

    public Uni<Either<RepositoryError, Boolean>> deletePropertyType(UUID propertyTypeId, UUID userId) {
        return findByIdAndUserId(propertyTypeId, userId)
                .onItem()
                .transformToUni(propertyTypeOpt -> {
                    if (propertyTypeOpt.isEmpty()) {
                        return Uni.createFrom().item(Either.left(new RepositoryError.NotFound("Property type not found")));
                    }
                    return delete(propertyTypeOpt.get())
                            .replaceWith(Either.right(true));
                });
    }

    public Uni<Either<RepositoryError, List<PropertyType>>> findAllByUserId(UUID userId) {
        return find("""
                FROM PropertyType pt 
                LEFT JOIN FETCH pt.createdBy u
                LEFT JOIN FETCH u.gender g
                LEFT JOIN FETCH u.role r
                WHERE pt.createdBy.id = ?1
                """, userId)
            .list()
            .onItem()
            .transform(propertyTypes -> Either.<RepositoryError, List<PropertyType>>right(propertyTypes))
            .onFailure()
            .recoverWithItem(e -> Either.<RepositoryError, List<PropertyType>>left(
                new RepositoryError.FetchFailed("Failed to retrieve property types: " + e.getMessage())
            ));
    }

    public Uni<Either<RepositoryError, PropertyType>> updatePropertyType(PropertyType propertyType, UUID userId) {
        String updateQuery = """
            UPDATE PropertyType pt 
            SET pt.detail = ?1, pt.updatedAt = ?2 
            WHERE pt.id = ?3 AND pt.createdBy.id = ?4
            """;
        
        return update(updateQuery, 
                propertyType.getDetail().trim(),
                LocalDateTime.now(),
                propertyType.getId(),
                userId)
            .chain(updatedCount -> {
                if (updatedCount == 0) {
                    return Uni.createFrom().item(
                        Either.left(new RepositoryError.NotFound("Property type not found or not updated"))
                    );
                }
                return find("""
                        FROM PropertyType pt
                        LEFT JOIN FETCH pt.createdBy u
                        LEFT JOIN FETCH u.gender g
                        LEFT JOIN FETCH u.role r
                        WHERE pt.id = ?1 AND pt.createdBy.id = ?2
                        """, propertyType.getId(), userId)
                    .firstResult()
                    .map(updatedPropertyType -> 
                        updatedPropertyType != null
                        ? Either.<RepositoryError, PropertyType>right(updatedPropertyType)
                        : Either.<RepositoryError, PropertyType>left(new RepositoryError.NotFound("Property type not found or not updated"))
                    );
            });
    }
}
