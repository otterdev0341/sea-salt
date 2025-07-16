package com.otterdev.infrastructure.repository;

import com.otterdev.domain.entity.Role;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoleRepository implements PanacheRepository<Role> {

    // Define methods for interacting with the Role entity, if needed.
    // For example, you might want to add methods to find roles by name or permissions.

    // Example method to find a role by its name
    public Uni<Role> findByName(String name) {
        return find("name", name).firstResult();
    }

    // You can also add methods for creating, updating, and deleting roles as needed.
    
}
