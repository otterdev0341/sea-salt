package com.otterdev.infrastructure.repository;

import com.otterdev.domain.entity.relation.PropertyFileDetail;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PropertyFileDetailRepository implements PanacheRepository<PropertyFileDetail> {
    
    // This repository can be used to manage FileDetail entities related to properties.
    // Additional methods specific to PropertyFileDetail can be added here if needed.
    
}
