package com.otterdev.domain.repository;

import com.otterdev.domain.entity.FileDetail;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileDetailRepository implements PanacheRepository<FileDetail> {
    
}
