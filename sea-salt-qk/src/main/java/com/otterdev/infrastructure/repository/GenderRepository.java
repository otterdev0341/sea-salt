package com.otterdev.infrastructure.repository;

import com.otterdev.domain.entity.Gender;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class GenderRepository implements PanacheRepository<Gender> {
    
}
