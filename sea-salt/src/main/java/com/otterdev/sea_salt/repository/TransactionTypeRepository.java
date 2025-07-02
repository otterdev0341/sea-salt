package com.otterdev.sea_salt.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.otterdev.sea_salt.entity.TransactionType;

@Repository
public interface TransactionTypeRepository extends ReactiveCrudRepository<TransactionType, UUID> {
    // Additional query methods can be defined here if needed
    // For example, you might want to find transaction types by their detail or createdBy field
    
}
