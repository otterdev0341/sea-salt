package com.otterdev.infrastructure.repository;

import com.otterdev.domain.entity.PropertyType;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PropertyTypeRepository implements PanacheRepository<PropertyType> {
    
}
