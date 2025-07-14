package com.otterdev.domain.repository;

import com.otterdev.domain.entity.InvestmentTransaction;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InvestmentTransactionRepository implements PanacheRepository<InvestmentTransaction> {
    
}
