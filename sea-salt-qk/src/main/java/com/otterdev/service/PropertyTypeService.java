package com.otterdev.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.otterdev.dto.entity.propertyType.ReqCreateUpdatePropertyTypeDto;
import com.otterdev.entity.table.PropertyType;
import com.otterdev.entity.table.User;
import com.otterdev.error.service.ServiceError;
import com.otterdev.error.service.ValidationError;
import com.otterdev.error.service.ResourceNotFoundError;
import com.otterdev.error.service.UnexpectedError;
import com.otterdev.repository.table.PropertyTypeRepository;
import com.otterdev.repository.table.UserRepository;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PropertyTypeService {

    @Inject
    private PropertyTypeRepository propertyTypeRepository;

    @Inject
    private UserRepository userRepository;

    // Create a new PropertyType
    @WithTransaction
    public Uni<Either<ServiceError, PropertyType>> newPropertyType(ReqCreateUpdatePropertyTypeDto propertyTypeDto, UUID userId) {
        if (propertyTypeDto == null || propertyTypeDto.getDetail() == null || propertyTypeDto.getDetail().trim().isEmpty()) {
            return Uni.createFrom().item(Either.left(new ValidationError("Invalid property type data: description cannot be empty")));
        }

        return userRepository.findById(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, PropertyType>left(
                        new ResourceNotFoundError("User not found with ID: " + userId)
                    ));
                }

                User user = userOpt.get();

                // Check if the user already has a property type with the same detail
                return propertyTypeRepository.existsByDetailAndCreatedBy(propertyTypeDto.getDetail().trim(), userId)
                    .chain(exists -> {
                        if (exists) {
                            return Uni.createFrom().item(Either.<ServiceError, PropertyType>left(
                                new ValidationError("Property type with detail '" + propertyTypeDto.getDetail().trim() + "' already exists for this user")
                            ));
                        }

                        LocalDateTime now = LocalDateTime.now();

                        PropertyType newPropertyType = PropertyType.builder()
                            .detail(propertyTypeDto.getDetail().trim())
                            .createdBy(user)
                            .createdAt(now)
                            .updatedAt(now)
                            .build();

                        return propertyTypeRepository.persist(newPropertyType)
                            .map(savedPropertyType -> Either.<ServiceError, PropertyType>right(savedPropertyType))
                            .onFailure().recoverWithItem(throwable -> 
                                Either.<ServiceError, PropertyType>left(
                                    new UnexpectedError("Failed to create property type: " + throwable.getMessage(), throwable)
                                )
                            );
                    });
            });
    }

    // Edit an existing PropertyType
    @WithTransaction
    public Uni<Either<ServiceError, PropertyType>> editPropertyType(UUID propertyTypeId, ReqCreateUpdatePropertyTypeDto propertyTypeDto, UUID userId) {
        if (propertyTypeDto == null || propertyTypeDto.getDetail() == null || propertyTypeDto.getDetail().trim().isEmpty()) {
            return Uni.createFrom().item(Either.left(new ValidationError("Invalid property type data: description cannot be empty")));
        }

        return propertyTypeRepository.findById(propertyTypeId)
            .chain(propertyTypeOpt -> {
                if (propertyTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, PropertyType>left(
                        new ResourceNotFoundError("Property type not found with ID: " + propertyTypeId)
                    ));
                }

                PropertyType propertyType = propertyTypeOpt.get();

                if (!propertyType.getCreatedBy().getId().equals(userId)) {
                    return Uni.createFrom().item(Either.<ServiceError, PropertyType>left(
                        new ValidationError("You don't have permission to update this property type")
                    ));
                }

                // Check if the user already has a property type with the same detail (excluding the current one)
                return propertyTypeRepository.existsByDetailAndCreatedBy(propertyTypeDto.getDetail().trim(), userId)
                    .chain(exists -> {
                        if (exists) {
                            return Uni.createFrom().item(Either.<ServiceError, PropertyType>left(
                                new ValidationError("Property type with detail '" + propertyTypeDto.getDetail().trim() + "' already exists for this user")
                            ));
                        }

                        propertyType.setDetail(propertyTypeDto.getDetail().trim());
                        propertyType.setUpdatedAt(LocalDateTime.now());

                        return propertyTypeRepository.persist(propertyType)
                            .map(updatedPropertyType -> Either.<ServiceError, PropertyType>right(updatedPropertyType))
                            .onFailure().recoverWithItem(throwable -> 
                                Either.<ServiceError, PropertyType>left(
                                    new UnexpectedError("Failed to update property type: " + throwable.getMessage(), throwable)
                                )
                            );
                    });
            });
    }

    // Delete a PropertyType
    @WithTransaction
    public Uni<Either<ServiceError, Boolean>> removePropertyType(UUID propertyTypeId, UUID userId) {
        return propertyTypeRepository.findById(propertyTypeId)
            .chain(propertyTypeOpt -> {
                if (propertyTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, Boolean>left(
                        new ResourceNotFoundError("Property type not found with ID: " + propertyTypeId)
                    ));
                }

                PropertyType propertyType = propertyTypeOpt.get();

                if (!propertyType.getCreatedBy().getId().equals(userId)) {
                    return Uni.createFrom().item(Either.<ServiceError, Boolean>left(
                        new ValidationError("You don't have permission to delete this property type")
                    ));
                }

                return propertyTypeRepository.delete(propertyType)
                    .map(deleted -> Either.<ServiceError, Boolean>right(true))
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, Boolean>left(
                            new UnexpectedError("Failed to delete property type: " + throwable.getMessage(), throwable)
                        )
                    );
            });
    }

    // View a PropertyType by ID
    @WithSession
    public Uni<Either<ServiceError, Optional<PropertyType>>> viewPropertyTypeById(UUID propertyTypeId, UUID userId) {
        return propertyTypeRepository.findById(propertyTypeId)
            .map(propertyTypeOpt -> {
                if (propertyTypeOpt.isEmpty()) {
                    return Either.<ServiceError, Optional<PropertyType>>left(
                        new ResourceNotFoundError("Property type not found with ID: " + propertyTypeId)
                    );
                }

                PropertyType propertyType = propertyTypeOpt.get();

                if (!propertyType.getCreatedBy().getId().equals(userId)) {
                    return Either.<ServiceError, Optional<PropertyType>>left(
                        new ValidationError("You don't have permission to view this property type")
                    );
                }

                return Either.<ServiceError, Optional<PropertyType>>right(Optional.of(propertyType));
            })
            .onFailure().recoverWithItem(throwable -> 
                Either.<ServiceError, Optional<PropertyType>>left(
                    new UnexpectedError("Failed to retrieve property type: " + throwable.getMessage(), throwable)
                )
            );
    }

    // View all PropertyTypes for a User
    @WithSession
    public Uni<Either<ServiceError, List<PropertyType>>> viewAllUserPropertyTypes(UUID userId) {
        return userRepository.findById(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, List<PropertyType>>left(
                        new ResourceNotFoundError("User not found with ID: " + userId)
                    ));
                }

                User user = userOpt.get();
                return propertyTypeRepository.find("createdBy", user).list()
                    .map(propertyTypes -> Either.<ServiceError, List<PropertyType>>right(propertyTypes))
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, List<PropertyType>>left(
                            new UnexpectedError("Failed to retrieve user property types: " + throwable.getMessage(), throwable)
                        )
                    );
            });
    }
}
