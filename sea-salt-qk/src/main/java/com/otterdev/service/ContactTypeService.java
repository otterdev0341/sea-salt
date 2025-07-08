package com.otterdev.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.otterdev.dto.entity.contactType.ReqCreateUpdateContactTypeDto;
import com.otterdev.entity.table.ContactType;
import com.otterdev.entity.table.User;
import com.otterdev.error.service.ServiceError;
import com.otterdev.error.service.ValidationError;
import com.otterdev.error.service.ResourceNotFoundError;
import com.otterdev.error.service.UnexpectedError;
import com.otterdev.repository.table.ContactTypeRepository;
import com.otterdev.repository.table.UserRepository;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ContactTypeService {
    
    @Inject
    ContactTypeRepository contactTypeRepository;

    @Inject
    UserRepository userRepository;

    @WithTransaction
    public Uni<Either<ServiceError, ContactType>> newContactType(ReqCreateUpdateContactTypeDto contactTypeDto, UUID userId) {
        // Validate input
        if (contactTypeDto == null || contactTypeDto.getDescription() == null || contactTypeDto.getDescription().trim().isEmpty()) {
            return Uni.createFrom().item(Either.left(new ValidationError("Invalid contact type data: description cannot be empty")));
        }

        // First, fetch the User entity to ensure it exists and for proper relationship mapping
        return userRepository.findById(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, ContactType>left(
                        new ResourceNotFoundError("User not found with ID: " + userId)
                    ));
                }
                
                User user = userOpt.get();
                LocalDateTime now = LocalDateTime.now();
                
                // Create new ContactType entity with proper User object and timestamps
                ContactType newContactType = ContactType.builder()
                    .description(contactTypeDto.getDescription().trim())
                    .createdBy(user)
                    .createdAt(now)
                    .updatedAt(now)
                    .build();
                
                // Save to repository
                return contactTypeRepository.persist(newContactType)
                    .map(savedContactType -> Either.<ServiceError, ContactType>right(savedContactType))
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, ContactType>left(
                            new UnexpectedError("Failed to create contact type: " + throwable.getMessage(), throwable)
                        )
                    );
            });
    }// end of newContactType

    // Additional CRUD methods for ContactType management
    
    @WithTransaction
    public Uni<Either<ServiceError, ContactType>> editContactType(UUID contactTypeId, ReqCreateUpdateContactTypeDto contactTypeDto, UUID userId) {
        // Validate input
        if (contactTypeDto == null || contactTypeDto.getDescription() == null || contactTypeDto.getDescription().trim().isEmpty()) {
            return Uni.createFrom().item(Either.left(new ValidationError("Invalid contact type data: description cannot be empty")));
        }

        return contactTypeRepository.findById(contactTypeId)
            .chain(contactTypeOpt -> {
                if (contactTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, ContactType>left(
                        new ResourceNotFoundError("Contact type not found with ID: " + contactTypeId)
                    ));
                }
                
                ContactType contactType = contactTypeOpt.get();
                
                // Check if user has permission to update (only creator can update)
                if (!contactType.getCreatedBy().getId().equals(userId)) {
                    return Uni.createFrom().item(Either.<ServiceError, ContactType>left(
                        new ValidationError("You don't have permission to update this contact type")
                    ));
                }
                
                // Update fields
                contactType.setDescription(contactTypeDto.getDescription().trim());
                contactType.setUpdatedAt(LocalDateTime.now());
                
                return contactTypeRepository.persist(contactType)
                    .map(updatedContactType -> Either.<ServiceError, ContactType>right(updatedContactType))
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, ContactType>left(
                            new UnexpectedError("Failed to update contact type: " + throwable.getMessage(), throwable)
                        )
                    );
            });
    }

    @WithTransaction
    public Uni<Either<ServiceError, Boolean>> removeContactType(UUID contactTypeId, UUID userId) {
        return contactTypeRepository.findById(contactTypeId)
            .chain(contactTypeOpt -> {
                if (contactTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, Boolean>left(
                        new ResourceNotFoundError("Contact type not found with ID: " + contactTypeId)
                    ));
                }
                
                ContactType contactType = contactTypeOpt.get();
                
                // Check if user has permission to delete (only creator can delete)
                if (!contactType.getCreatedBy().getId().equals(userId)) {
                    return Uni.createFrom().item(Either.<ServiceError, Boolean>left(
                        new ValidationError("You don't have permission to delete this contact type")
                    ));
                }
                
                return contactTypeRepository.delete(contactType)
                    .map(deleted -> Either.<ServiceError, Boolean>right(true))
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, Boolean>left(
                            new UnexpectedError("Failed to delete contact type: " + throwable.getMessage(), throwable)
                        )
                    );
            });
    }

    @WithSession
    public Uni<Either<ServiceError, Optional<ContactType>>> viewContactTypeById(UUID contactTypeId, UUID userId) {
        return contactTypeRepository.findById(contactTypeId)
            .map(contactTypeOpt -> {
                if (contactTypeOpt.isEmpty()) {
                    return Either.<ServiceError, Optional<ContactType>>left(
                        new ResourceNotFoundError("Contact type not found with ID: " + contactTypeId)
                    );
                }
                
                ContactType contactType = contactTypeOpt.get();
                
                // Check if user has permission to view (only creator can view)
                if (!contactType.getCreatedBy().getId().equals(userId)) {
                    return Either.<ServiceError, Optional<ContactType>>left(
                        new ValidationError("You don't have permission to view this contact type")
                    );
                }
                
                return Either.<ServiceError, Optional<ContactType>>right(Optional.of(contactType));
            })
            .onFailure().recoverWithItem(throwable -> 
                Either.<ServiceError, Optional<ContactType>>left(
                    new UnexpectedError("Failed to retrieve contact type: " + throwable.getMessage(), throwable)
                )
            );
    }

    @WithSession
    public Uni<Either<ServiceError, List<ContactType>>> viewAllUserContactTypes(UUID userId) {
        return userRepository.findById(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, java.util.List<ContactType>>left(
                        new ResourceNotFoundError("User not found with ID: " + userId)
                    ));
                }
                
                User user = userOpt.get();
                return contactTypeRepository.find("createdBy", user).list()
                    .map(contactTypes -> Either.<ServiceError, java.util.List<ContactType>>right(contactTypes))
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, java.util.List<ContactType>>left(
                            new UnexpectedError("Failed to retrieve user contact types: " + throwable.getMessage(), throwable)
                        )
                    );
            });
    }
}
