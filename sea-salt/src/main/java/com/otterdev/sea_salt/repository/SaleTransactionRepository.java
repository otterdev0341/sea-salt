package com.otterdev.sea_salt.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.otterdev.sea_salt.entity.transaction.SaleTransaction;

@Repository
public interface SaleTransactionRepository extends ReactiveCrudRepository<SaleTransaction, UUID> {
    // Additional query methods can be defined here if needed

    

    
}
