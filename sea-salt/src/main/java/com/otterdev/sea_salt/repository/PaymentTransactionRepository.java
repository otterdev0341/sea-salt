package com.otterdev.sea_salt.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.otterdev.sea_salt.entity.transaction.PaymentTransaction;


@Repository
public interface PaymentTransactionRepository extends ReactiveCrudRepository<PaymentTransaction, UUID> {
    // Additional query methods can be defined here if needed
    // For example, you might want to find payment transactions by their transactionId or userId
    
}
