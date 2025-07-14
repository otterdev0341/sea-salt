package com.otterdev.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import com.otterdev.domain.entity.User;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    // This class can be used to define custom query methods if needed.
    // For example, you can add methods like findByEmail, findByUsername, etc.
    
    // Example:
    // public Uni<User> findByEmail(String email) {
    //     return find("email", email).firstResult();
    // }
    public Uni<Boolean> existsByUsername(String username) {
        return find("username", username)
            .firstResult()
            .onItem().transform(user -> user != null);
    }
    public Uni<Boolean> existsByEmail(String email) {
        return find("email", email)
                .firstResult()
                .onItem().transform(user -> user != null);
    }

    public Uni<Optional<User>> findByEmail(String email) {
        return find("email", email.trim().toLowerCase())
                .firstResult()
                .map(Optional::ofNullable);
    }

    
    public Uni<Optional<User>> findById(UUID id) {
        return find("id", id)
                .firstResult()
                .map(Optional::ofNullable);
    }
}
