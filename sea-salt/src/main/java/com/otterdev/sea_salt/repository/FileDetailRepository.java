package com.otterdev.sea_salt.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.otterdev.sea_salt.entity.FileDetail;

@Repository
public interface FileDetailRepository extends ReactiveCrudRepository<FileDetail, UUID> {
    
}
