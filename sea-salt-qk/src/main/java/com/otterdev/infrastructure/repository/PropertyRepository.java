package com.otterdev.infrastructure.repository;

import com.otterdev.domain.entity.Property;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PropertyRepository implements PanacheRepository<Property> {
    
}
