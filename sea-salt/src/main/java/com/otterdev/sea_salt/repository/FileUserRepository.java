package com.otterdev.sea_salt.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.otterdev.sea_salt.entity.join.FileUser;

@Repository
public interface FileUserRepository extends ReactiveCrudRepository<FileUser, UUID> {

    // Additional methods can be defined here if needed
    
}
