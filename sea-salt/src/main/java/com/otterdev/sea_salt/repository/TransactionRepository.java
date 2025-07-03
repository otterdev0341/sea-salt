package com.otterdev.sea_salt.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.otterdev.sea_salt.entity.transaction.Transaction;

@Repository
public interface TransactionRepository extends ReactiveCrudRepository<Transaction, UUID> {
    // Additional query methods can be defined here if needed
    // For example, you might want to find transactions by their type or status
    
}
