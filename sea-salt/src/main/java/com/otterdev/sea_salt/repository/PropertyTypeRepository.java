package com.otterdev.sea_salt.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.otterdev.sea_salt.entity.PropertyType;

import reactor.core.publisher.Mono;

@Repository
public interface PropertyTypeRepository extends ReactiveCrudRepository<PropertyType, UUID> {
    // Additional query methods can be defined here if needed
    public Mono<Boolean> existsByDetail(String detail);
    
}
