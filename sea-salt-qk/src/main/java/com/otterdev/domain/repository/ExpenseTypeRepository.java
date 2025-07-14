package com.otterdev.domain.repository;

import com.otterdev.domain.entity.ExpenseType;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseTypeRepository implements PanacheRepository<ExpenseType> {
    
}
