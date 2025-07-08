package com.otterdev.repository.table;

import java.util.Optional;
import java.util.UUID;

import com.otterdev.entity.table.ContactType;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ContactTypeRepository implements PanacheRepository<ContactType> {

    // Custom method for UUID-based operations
    public Uni<Optional<ContactType>> findById(UUID id) {
        return find("id", id).firstResult()
                .map(Optional::ofNullable);
    }
}
