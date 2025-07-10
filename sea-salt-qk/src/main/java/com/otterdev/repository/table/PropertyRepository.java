package com.otterdev.repository.table;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.otterdev.entity.table.Property;
import com.otterdev.error.repository.FetchFailedError;
import com.otterdev.error.repository.RepositoryError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class PropertyRepository implements PanacheRepository<Property> {

    // This class can be used to define custom query methods if needed.
    // For example, you can add methods like findByKey, findByValue, etc.
   public Uni<Optional<Property>> findById(UUID id) {
        return find("id", id).firstResult()
                .map(Optional::ofNullable);
    }

    // You can also add methods for creating, updating, and deleting properties
    // if you need specific logic beyond the default PanacheRepository methods.
    public Uni<Either<RepositoryError, List<Property>>> findAllUserPropertiesByUserId(UUID userId) {
        return find("ownerBy.id", userId).list()
                .map(properties -> Either.<RepositoryError, List<Property>>right(properties))
                .onFailure().recoverWithItem(e -> Either.<RepositoryError, List<Property>>left(new FetchFailedError("Failed to fetch properties: " + e.getMessage())));
    }

    public Uni<Either<RepositoryError, List<Property>>> findPropertiesByStatusAndUserId(UUID statusId, UUID userId) {
    return find("status.id = ?1 and createdBy = ?2", statusId, userId).list()
            .map(properties -> Either.<RepositoryError, List<Property>>right(properties))
            .onFailure().recoverWithItem(e -> Either.<RepositoryError, List<Property>>left(new FetchFailedError("Failed to fetch properties by status: " + e.getMessage())));
}
}
