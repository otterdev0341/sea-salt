package com.otterdev.domain.repository;

import com.otterdev.domain.entity.SaleTransaction;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SaleTransactionRepository implements PanacheRepository<SaleTransaction> {
    
}
