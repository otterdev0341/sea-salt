package com.otterdev.sea_salt.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.otterdev.sea_salt.entity.PropertyStatus;

import reactor.core.publisher.Mono;

@Repository
public interface PropertyStatusRepository extends ReactiveCrudRepository<PropertyStatus, UUID> {

    /**
     * Find a property status by detail.
     *
     * @param detail the detail of the property status
     * @return a Mono containing the PropertyStatus if found, or empty if not found
     */
    Mono<PropertyStatus> findByDetail(String detail);
    
}
