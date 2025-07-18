package com.otterdev.infrastructure.repository;

import java.util.Optional;

import com.otterdev.domain.entity.Role;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoleRepository implements PanacheRepository<Role> {

    // Define methods for interacting with the Role entity, if needed.
    // For example, you might want to add methods to find roles by name or permissions.

    // Example method to find a role by its name
    public Uni<Optional<Role>> findByDetail(String detail) {
        return find("detail", detail.trim())
                .firstResult()
                .onItem()
                .transform(Optional::ofNullable);

    }

    public Uni<Role> getUserRole() {
        return find("detail", "user")
                .firstResult();
                
    }
    
}
