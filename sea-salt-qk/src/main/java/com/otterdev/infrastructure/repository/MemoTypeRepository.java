package com.otterdev.infrastructure.repository;

import com.otterdev.domain.entity.MemoType;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MemoTypeRepository implements PanacheRepository<MemoType> {
    
}
