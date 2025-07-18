package com.otterdev.infrastructure.repository;

import java.util.Optional;

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

}
