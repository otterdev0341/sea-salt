package com.otterdev.sea_salt.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.otterdev.sea_salt.entity.ContactType;

import reactor.core.publisher.Mono;

@Repository
public interface ContactTypeRepository extends ReactiveCrudRepository<ContactType, UUID> {

    /**
     * Find a contact type by description.
     *
     * @param description the description of the contact type
     * @return a Mono containing the ContactType if found, or empty if not found
     */
    Mono<ContactType> findByDescription(String description);
    
}
