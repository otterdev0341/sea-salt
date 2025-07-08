package com.otterdev.repository.table;

import java.util.Optional;
import java.util.UUID;

import com.otterdev.entity.table.Gender;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GenderRepository implements PanacheRepository<Gender> {
    
    public Uni<Gender> findByName(String name) {
        return find("name", name).firstResult();
    }

    public Uni<Optional<Gender>> findById(UUID id) {
        return find("id", id).firstResult()
                .map(Optional::ofNullable);
    }

}
