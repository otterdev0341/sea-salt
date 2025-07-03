package com.otterdev.sea_salt.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.otterdev.sea_salt.entity.join.MemoProperty;

@Repository
public interface MemoPropertyRepository extends ReactiveCrudRepository<MemoProperty, UUID> {
    // Additional methods can be defined here if needed
    
}
