package com.otterdev.infrastructure.repository;

import com.otterdev.domain.entity.Memo;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MemoRepository implements PanacheRepository<Memo> {
    
}
