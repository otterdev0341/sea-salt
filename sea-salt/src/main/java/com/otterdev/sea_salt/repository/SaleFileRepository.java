package com.otterdev.sea_salt.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.otterdev.sea_salt.entity.join.SaleFile;

public interface SaleFileRepository extends ReactiveCrudRepository<SaleFile, UUID> {
    // Additional methods can be defined here if needed
    
}
