package com.otterdev.repository.table;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.otterdev.entity.table.PropertyStatus;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class PropertyStatusRepository implements PanacheRepository<PropertyStatus> {

    // This class can be used to define custom query methods if needed.
    // For example, you can add methods like findByStatus, findByPropertyId, etc.
    
    // check is user already have this property status
    public Uni<Boolean> existsByStatusDetailAndUserId(String propertyDetail, UUID userId) {
        return find("property.detail = ?1 and user.created_by = ?2", propertyDetail, userId)
                .firstResult()
                .map(result -> result != null);
    }

    public Uni<Optional<PropertyStatus>> findById(UUID id) {
        return find("id", id).firstResult()
                .map(Optional::ofNullable);
    }

    public Uni<List<PropertyStatus>> findByCreatedById(UUID userId) {
        return find("createdBy.id", userId).list();
    }
    

}
