package com.otterdev.sea_salt.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.otterdev.sea_salt.entity.MemoType;

import reactor.core.publisher.Mono;

public interface MemoTypeRepository extends ReactiveCrudRepository<MemoType, UUID> {
    // Additional query methods can be defined here if needed
    public Mono<Boolean> existsByDetail(String detail);
    
}
