package com.otterdev.infrastructure.repository;

import java.util.Optional;

import com.otterdev.domain.entity.Gender;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class GenderRepository implements PanacheRepository<Gender> {
    
    public Uni<Optional<Gender>> findBydetail(String detail) {
        return find("detail", detail.trim())
                .firstResult()
                .onItem()
                .transform(Optional::ofNullable);
    }

    

}
