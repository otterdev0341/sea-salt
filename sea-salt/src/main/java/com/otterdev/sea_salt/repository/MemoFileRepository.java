package com.otterdev.sea_salt.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.otterdev.sea_salt.entity.join.MemoFile;

@Repository
public interface MemoFileRepository extends ReactiveCrudRepository<MemoFile, UUID> {
    
    // Additional methods can be defined here if needed
    
}
