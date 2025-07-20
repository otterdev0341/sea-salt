package com.otterdev.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.otterdev.domain.entity.Property;
import com.otterdev.error_structure.RepositoryError;
import com.spencerwi.either.Either;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PropertyRepository implements PanacheRepository<Property> {
    


    public Uni<Optional<Property>> findByIdAndUserId(UUID propertyId, UUID userId) {
        return find("FROM Property p WHERE p.id = ?1 AND p.createdBy.id = ?2", propertyId, userId)
            .firstResult()
            .map(Optional::ofNullable);
    }


    public Uni<Boolean> isExistByNameAndUserId(String name, UUID userId) {
        return count("name = ?1 AND createdBy.id = ?2", name, userId)
            .map(count -> count > 0);
    }

    public Uni<Either<RepositoryError, Boolean>> deleteProperty(UUID propertyId, UUID userId) {
        return findByIdAndUserId(propertyId, userId)
                .onItem()
                .transformToUni(propertyOpt -> {
                    if (propertyOpt.isEmpty()) {
                        return Uni.createFrom().item(Either.left(new RepositoryError.NotFound("Property not found")));
                    }
                    return delete(propertyOpt.get())
                            .replaceWith(Either.right(true));
                });
    }

    public Uni<Either<RepositoryError, List<Property>>> findAllByUserId(UUID userId) {
        return find("""
                FROM Property p 
                LEFT JOIN FETCH c.createdBy u
                LEFT JOIN FETCH u.gender g
                LEFT JOIN FETCH u.role r
                WHERE c.createdBy.id = ?1
                """, userId)
            .list()
            .onItem()
            .transform(properties -> Either.<RepositoryError, List<Property>>right(properties))
            .onFailure()
            .recoverWithItem(e -> Either.<RepositoryError, List<Property>>left(
                new RepositoryError.FetchFailed("Failed to retrieve properties: " + e.getMessage())
            ));
    }

    public Uni<Either<RepositoryError, Property>> updateProperty(Property property, UUID userId) {

    String updateQuery = """
        UPDATE Property p
        SET p.name = ?1, p.description = ?2, p.specific = ?3, p.hilight = ?4,
            p.area = ?5, p.price = ?6, p.fsp = ?7, p.status = ?8,
            p.ownerBy = ?9, p.mapUrl = ?10, p.lat = ?11, p.lng = ?12,
            p.updatedAt = ?13
        WHERE p.id = ?14 AND p.createdBy.id = ?15
        """;

    return update(updateQuery,
            property.getName().trim(),
            property.getDescription(),
            property.getSpecific(),
            property.getHilight(),
            property.getArea(),
            property.getPrice(),
            property.getFsp(),
            property.getStatus(),
            property.getOwnerBy(),
            property.getMapUrl(),
            property.getLat(),
            property.getLng(),
            LocalDateTime.now(),
            property.getId(),
            userId
        ).chain(updatedCount -> {
            if (updatedCount == 0) {
                return Uni.createFrom().item(
                    Either.left(new RepositoryError.NotFound("Property not found or not updated"))
                );
            }

            // Fetch the updated property with all necessary relationships
            return find("""
                    FROM Property p
                    LEFT JOIN FETCH p.status s
                    LEFT JOIN FETCH p.ownerBy c
                    LEFT JOIN FETCH p.createdBy u
                    LEFT JOIN FETCH u.gender g
                    LEFT JOIN FETCH u.role r
                    WHERE p.id = ?1 AND p.createdBy.id = ?2
                    """, property.getId(), userId)
                .firstResult()
                .map(updatedProperty ->
                    updatedProperty != null
                        ? Either.<RepositoryError, Property>right(updatedProperty)
                        : Either.<RepositoryError, Property>left(new RepositoryError.NotFound("Property not found or not updated"))
                );
        });
    }


    public Uni<Either<RepositoryError, List<Property>>> findAllPropertiesByTypeAndUserId(UUID propertyTypeId, UUID userId) {
        String query = """
            SELECT DISTINCT p
            FROM PropertyPropertyType ppt
            JOIN ppt.property p
            LEFT JOIN FETCH p.status s
            LEFT JOIN FETCH p.ownerBy c
            LEFT JOIN FETCH p.createdBy u
            LEFT JOIN FETCH u.gender g
            LEFT JOIN FETCH u.role r
            WHERE ppt.propertyType.id = ?1 AND ppt.createdBy.id = ?2
            """;

        return find(query, propertyTypeId, userId)
            .list()
            .map(properties -> {
                if (properties == null || properties.isEmpty()) {
                    return Either.<RepositoryError, List<Property>>left(
                        new RepositoryError.NotFound("No properties found for the given type and user")
                    );
                }
                return Either.<RepositoryError, List<Property>>right(properties);
            })
            .onFailure()
            .recoverWithItem(err ->
                Either.<RepositoryError, List<Property>>left(
                    new RepositoryError.FetchFailed(err.getMessage())
                )
            );
    }

    public Uni<Either<RepositoryError, List<Property>>> findAllPropertiesByStatusAndUserId(UUID statusId, UUID userId) {
        String query = """
            SELECT DISTINCT p
            FROM Property p
            LEFT JOIN FETCH p.status s
            LEFT JOIN FETCH p.ownerBy c
            LEFT JOIN FETCH p.createdBy u
            LEFT JOIN FETCH u.gender g
            LEFT JOIN FETCH u.role r
            WHERE p.status.id = ?1 AND p.createdBy.id = ?2
            """;

        return find(query, statusId, userId)
            .list()
            .map(properties -> {
                if (properties == null || properties.isEmpty()) {
                    return Either.<RepositoryError, List<Property>>left(
                        new RepositoryError.NotFound("No properties found for the given status and user")
                    );
                }
                return Either.<RepositoryError, List<Property>>right(properties);
            })
            .onFailure()
            .recoverWithItem(err ->
                Either.<RepositoryError, List<Property>>left(
                    new RepositoryError.FetchFailed(err.getMessage())
                )
            );
    }

    public Uni<Either<RepositoryError, List<Property>>> getAllPropertiesBySold(Boolean sold, UUID userId) {
        String query = """
            SELECT DISTINCT p
            FROM Property p
            LEFT JOIN FETCH p.status s
            LEFT JOIN FETCH p.ownerBy c
            LEFT JOIN FETCH p.createdBy u
            LEFT JOIN FETCH u.gender g
            LEFT JOIN FETCH u.role r
            WHERE p.sold = ?1 AND p.createdBy.id = ?2
            """;

        return find(query, sold, userId)
            .list()
            .map(properties -> {
                if (properties == null || properties.isEmpty()) {
                    return Either.<RepositoryError, List<Property>>left(
                        new RepositoryError.NotFound("No properties found for the given sold status and user")
                    );
                }
                return Either.<RepositoryError, List<Property>>right(properties);
            })
            .onFailure()
            .recoverWithItem(err ->
                Either.<RepositoryError, List<Property>>left(
                    new RepositoryError.FetchFailed(err.getMessage())
                )
            );
    }




} // end of PropertyRepository
