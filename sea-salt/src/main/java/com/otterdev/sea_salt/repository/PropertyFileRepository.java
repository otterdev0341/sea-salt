package com.otterdev.sea_salt.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.otterdev.sea_salt.entity.join.PropertyFile;

public interface PropertyFileRepository extends ReactiveCrudRepository<PropertyFile, UUID> {
    // Additional methods can be defined here if needed
    
}
