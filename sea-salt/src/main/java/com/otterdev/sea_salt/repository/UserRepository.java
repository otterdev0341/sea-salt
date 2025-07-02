package com.otterdev.sea_salt.repository;

import java.util.UUID;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.otterdev.sea_salt.entity.User;

import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, UUID> {

    /**
     * Find a user by email.
     *
     * @param email the email of the user
     * @return a Mono containing the User if found, or empty if not found
     */
    Mono<User> findByEmail(String email);

    /**
     * Find a user by first name and last name.
     *
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @return a Mono containing the User if found, or empty if not found
     */
    Mono<User> findByFirstNameAndLastName(String firstName, String lastName);
}
