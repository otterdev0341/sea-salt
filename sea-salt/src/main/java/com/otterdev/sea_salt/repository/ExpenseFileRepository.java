package com.otterdev.sea_salt.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.otterdev.sea_salt.entity.join.ExpenseFile;

public interface ExpenseFileRepository extends ReactiveCrudRepository<ExpenseFile, UUID> {
    
}
