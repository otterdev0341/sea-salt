package com.otterdev.repository.table;

import java.util.Optional;
import java.util.UUID;

import com.otterdev.entity.table.PropertyType;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class PropertyTypeRepository implements PanacheRepository<PropertyType> {

    // This class can be used to define custom query methods if needed.
    // For example, you can add methods like findByPropertyName, findByPropertyValue, etc.
    
    public Uni<Optional<PropertyType>> findById(UUID id) {
        return find("id", id).firstResult()
                .map(Optional::ofNullable);
    }

    public Uni<Boolean> existsByDetailAndCreatedBy(String detail, UUID createdBy) {
        return find("detail = ?1 and createdBy.id = ?2", detail, createdBy).count()
                .map(count -> count > 0);
    }

}
