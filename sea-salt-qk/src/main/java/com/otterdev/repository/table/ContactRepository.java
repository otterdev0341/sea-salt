package com.otterdev.repository.table;

import java.util.Optional;
import java.util.UUID;

import com.otterdev.entity.table.Contact;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class ContactRepository implements PanacheRepository<Contact> {
    
    public Uni<Optional<Contact>> findById(UUID id) {
        return find("id", id).firstResult()
                .map(Optional::ofNullable);
    }

}
