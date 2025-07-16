package com.otterdev.infrastructure.repository;

import com.otterdev.domain.entity.Contact;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ContactRepository implements PanacheRepository<Contact> {
    
}
