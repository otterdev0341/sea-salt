package com.otterdev.sea_salt.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.otterdev.sea_salt.entity.Property;

public interface PropertyRepository extends ReactiveCrudRepository<Property, UUID> {
    // Additional query methods can be defined here if needed
    // For example, you might want to find properties by their type or createdBy field
    
}
