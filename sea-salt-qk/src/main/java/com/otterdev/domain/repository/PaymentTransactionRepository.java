package com.otterdev.domain.repository;

import com.otterdev.domain.entity.PaymentTransaction;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PaymentTransactionRepository implements PanacheRepository<PaymentTransaction> {
    
}
