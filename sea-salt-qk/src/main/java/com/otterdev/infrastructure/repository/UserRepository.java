package com.otterdev.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;
import com.otterdev.domain.entity.User;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;



@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    
    public Uni<Optional<User>> findByEmail(String email) {
        return find("FROM User u LEFT JOIN FETCH u.role WHERE u.email = ?1", email.trim())
        .firstResult()
        .map(Optional::ofNullable);
                    
    }

    public Uni<Optional<User>> findByUserId(UUID userId) {
        return find("FROM User u LEFT JOIN FETCH u.role WHERE u.id = ?1", userId)
        .firstResult()
        .map(Optional::ofNullable);
    }

    public Uni<Boolean> existsByEmail(String email) {
        return find("FROM User u WHERE u.email = ?1", email.trim())
        .firstResult()
        .onItem().transform(user -> user != null);
    }

    public Uni<Boolean> existsByUsername(String username) {
        return find("FROM User u WHERE u.username = ?1", username.trim())
        .firstResult()
        .onItem().transform(user -> user != null);
    }

}
