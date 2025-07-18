package com.otterdev.infrastructure.repository;

import com.otterdev.domain.entity.relation.MemoFileDetail;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MemoFileDetailRepository implements PanacheRepository<MemoFileDetail> {
    
}
