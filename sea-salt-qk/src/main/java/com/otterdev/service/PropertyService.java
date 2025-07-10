package com.otterdev.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.otterdev.dto.entity.property.ReqCreatePropertyDto;
import com.otterdev.dto.entity.property.ReqUpdateProperty;
import com.otterdev.entity.table.Property;
import com.otterdev.entity.table.PropertyStatus;
import com.otterdev.entity.table.User;
import com.otterdev.error.service.ResourceNotFoundError;
import com.otterdev.error.service.ServiceError;
import com.otterdev.error.service.ValidationError;
import com.otterdev.error.service.UnexpectedError;
import com.otterdev.repository.table.PropertyRepository;
import com.otterdev.repository.table.PropertyStatusRepository;
import com.otterdev.repository.table.UserRepository;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PropertyService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private PropertyRepository propertyRepository;

    @Inject
    private PropertyStatusRepository propertyStatusRepository;

    // Create a new Property
    @WithTransaction
    public Uni<Either<ServiceError, Property>> newProperty(ReqCreatePropertyDto propertyDto, UUID userId) {
        if (propertyDto == null || propertyDto.getName() == null || propertyDto.getName().trim().isEmpty()) {
            return Uni.createFrom().item(Either.left(new ValidationError("Property name cannot be empty")));
        }

        return userRepository.findById(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, Property>left(
                        new ResourceNotFoundError("User not found with ID: " + userId)
                    ));
                }

                User user = userOpt.get();

                // Handle property status if provided (similar to editProperty pattern)
                if (propertyDto.getStatus() != null) {
                    return propertyStatusRepository.findById(propertyDto.getStatus())
                        .chain(statusOpt -> {
                            if (statusOpt.isEmpty()) {
                                return Uni.createFrom().item(Either.<ServiceError, Property>left(
                                    new ResourceNotFoundError("Property status not found with ID: " + propertyDto.getStatus())
                            ));
                            }

                            PropertyStatus status = statusOpt.get();
                            
                            // Create property with validated status
                            Property newProperty = buildPropertyFromCreateDto(propertyDto, user, userId, status);

                            return propertyRepository.persist(newProperty)
                                .map(savedProperty -> Either.<ServiceError, Property>right(savedProperty))
                                .onFailure().recoverWithItem(throwable -> 
                                    Either.<ServiceError, Property>left(
                                        new UnexpectedError("Failed to create property: " + throwable.getMessage(), throwable)
                                    )
                                );
                        });
                } else {
                    // Create property without status (status is null)
                    Property newProperty = buildPropertyFromCreateDto(propertyDto, user, userId, null);

                    return propertyRepository.persist(newProperty)
                        .map(savedProperty -> Either.<ServiceError, Property>right(savedProperty))
                        .onFailure().recoverWithItem(throwable -> 
                            Either.<ServiceError, Property>left(
                                new UnexpectedError("Failed to create property: " + throwable.getMessage(), throwable)
                            )
                        );
                }
            });
    }

    // Edit an existing Property
    @WithTransaction
    public Uni<Either<ServiceError, Property>> editProperty(UUID propertyId, ReqUpdateProperty propertyDto, UUID userId) {
        if (propertyDto == null) {
            return Uni.createFrom().item(Either.left(new ValidationError("Property data cannot be null")));
        }

        return propertyRepository.findById(propertyId)
            .chain(propertyOpt -> {
                if (propertyOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, Property>left(
                        new ResourceNotFoundError("Property not found with ID: " + propertyId)
                    ));
                }

                Property property = propertyOpt.get();

                if (!property.getCreatedBy().equals(userId)) {
                    return Uni.createFrom().item(Either.<ServiceError, Property>left(
                        new ValidationError("You don't have permission to update this property")
                    ));
                }

                // Handle property status if provided
                if (propertyDto.getStatus() != null) {
                    return propertyStatusRepository.findById(propertyDto.getStatus())
                        .chain(statusOpt -> {
                            if (statusOpt.isEmpty()) {
                                return Uni.createFrom().item(Either.<ServiceError, Property>left(
                                    new ResourceNotFoundError("Property status not found with ID: " + propertyDto.getStatus())
                            ));
                            }

                            PropertyStatus status = statusOpt.get();
                            property.setStatus(status);
                            
                            // Update other fields
                            updatePropertyFields(property, propertyDto);
                            property.setUpdatedAt(LocalDateTime.now());

                            return propertyRepository.persist(property)
                                .map(updatedProperty -> Either.<ServiceError, Property>right(updatedProperty))
                                .onFailure().recoverWithItem(throwable -> 
                                    Either.<ServiceError, Property>left(
                                        new UnexpectedError("Failed to update property: " + throwable.getMessage(), throwable)
                                    )
                                );
                        });
                } else {
                    // Update fields without status change
                    updatePropertyFields(property, propertyDto);
                    property.setUpdatedAt(LocalDateTime.now());

                    return propertyRepository.persist(property)
                        .map(updatedProperty -> Either.<ServiceError, Property>right(updatedProperty))
                        .onFailure().recoverWithItem(throwable -> 
                            Either.<ServiceError, Property>left(
                                new UnexpectedError("Failed to update property: " + throwable.getMessage(), throwable)
                            )
                        );
                }
            });
    }

    // Delete a Property
    @WithTransaction
    public Uni<Either<ServiceError, Boolean>> deleteProperty(UUID propertyId, UUID userId) {
        return propertyRepository.findById(propertyId)
            .chain(propertyOpt -> {
                if (propertyOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, Boolean>left(
                        new ResourceNotFoundError("Property not found with ID: " + propertyId)
                    ));
                }

                Property property = propertyOpt.get();

                if (!property.getCreatedBy().equals(userId)) {
                    return Uni.createFrom().item(Either.<ServiceError, Boolean>left(
                        new ValidationError("You don't have permission to delete this property")
                    ));
                }

                return propertyRepository.delete(property)
                    .map(deleted -> Either.<ServiceError, Boolean>right(true))
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, Boolean>left(
                            new UnexpectedError("Failed to delete property: " + throwable.getMessage(), throwable)
                        )
                    );
            });
    }

    // View a Property by ID
    @WithSession
    public Uni<Either<ServiceError, Property>> viewPropertyById(UUID propertyId, UUID userId) {
        return propertyRepository.findById(propertyId)
            .map(propertyOpt -> {
                if (propertyOpt.isEmpty()) {
                    return Either.<ServiceError, Property>left(
                        new ResourceNotFoundError("Property not found with ID: " + propertyId)
                    );
                }

                Property property = propertyOpt.get();

                if (!property.getCreatedBy().equals(userId)) {
                    return Either.<ServiceError, Property>left(
                        new ValidationError("You don't have permission to view this property")
                    );
                }

                return Either.<ServiceError, Property>right(property);
            })
            .onFailure().recoverWithItem(throwable -> 
                Either.<ServiceError, Property>left(
                    new UnexpectedError("Failed to retrieve property: " + throwable.getMessage(), throwable)
                )
            );
    }

    // View all Properties for a User
    @WithSession
    public Uni<Either<ServiceError, List<Property>>> viewAllUserProperties(UUID userId) {
        return userRepository.findById(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, List<Property>>left(
                        new ResourceNotFoundError("User not found with ID: " + userId)
                    ));
                }

                return propertyRepository.findAllUserPropertiesByUserId(userId)
                    .map(result -> {
                        if (result.isLeft()) {
                            // Convert RepositoryError to ServiceError
                            return Either.<ServiceError, List<Property>>left(
                                new UnexpectedError("Failed to retrieve user properties: " + result.getLeft().message())
                            );
                        }
                        // Return the successful result
                        return Either.<ServiceError, List<Property>>right(result.getRight());
                    })
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, List<Property>>left(
                            new UnexpectedError("Failed to retrieve user properties: " + throwable.getMessage(), throwable)
                        )
                    );
            });
    }

    @WithSession
public Uni<Either<ServiceError, List<Property>>> viewPropertiesByStatus(UUID statusId, UUID userId) {
    return propertyStatusRepository.findById(statusId)
        .chain(statusOpt -> {
            if (statusOpt.isEmpty()) {
                return Uni.createFrom().item(Either.<ServiceError, List<Property>>left(
                    new ResourceNotFoundError("Property status not found with ID: " + statusId)
                ));
            }

            return propertyRepository.findPropertiesByStatusAndUserId(statusId, userId)
                .map(result -> {
                    if (result.isLeft()) {
                        // Convert RepositoryError to ServiceError
                        return Either.<ServiceError, List<Property>>left(
                            new UnexpectedError("Failed to retrieve properties by status: " + result.getLeft().message())
                        );
                    }
                    // Return the successful result
                    return Either.<ServiceError, List<Property>>right(result.getRight());
                })
                .onFailure().recoverWithItem(throwable -> 
                    Either.<ServiceError, List<Property>>left(
                        new UnexpectedError("Failed to retrieve properties by status: " + throwable.getMessage(), throwable)
                    )
                );
        });
}

    // Updated helper method to build Property from Create DTO
    private Property buildPropertyFromCreateDto(ReqCreatePropertyDto propertyDto, User user, UUID userId, PropertyStatus status) {
        LocalDateTime now = LocalDateTime.now();
        
        Property.PropertyBuilder builder = Property.builder()
            .name(propertyDto.getName().trim())
            .ownerBy(user)
            .sold(false)
            .createdBy(userId)
            .createdAt(now)
            .updatedAt(now);

        // Set status if provided
        if (status != null) {
            builder.status(status);
        }

        // Set optional fields only if they are not null
        setIfNotNull(builder::description, propertyDto.getDescription());
        setIfNotNull(builder::specific, propertyDto.getSpecific());
        setIfNotNull(builder::hilight, propertyDto.getHilight());
        setIfNotNull(builder::area, propertyDto.getArea());
        setIfNotNull(builder::price, propertyDto.getPrice());
        setIfNotNull(builder::fsp, propertyDto.getFsp());
        setIfNotNull(builder::mapUrl, propertyDto.getMapUrl());
        setIfNotNull(builder::lat, propertyDto.getLat());
        setIfNotNull(builder::lng, propertyDto.getLng());

        return builder.build();
    }

    // Utility method for null-safe field setting
    private <T> void setIfNotNull(java.util.function.Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }

    // Enhanced utility method for null-safe field updating
    private <T> void updateIfNotNull(java.util.function.Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }

    // Overloaded utility method with transformation
    private <T> void updateIfNotNull(java.util.function.Consumer<T> setter, T value, java.util.function.Function<T, T> transformer) {
        if (value != null) {
            setter.accept(transformer.apply(value));
        }
    }

    // Helper method to update property fields from DTO
    private void updatePropertyFields(Property property, ReqUpdateProperty propertyDto) {
        // Update fields using utility methods
        updateIfNotNull(property::setName, propertyDto.getName(), String::trim);
        updateIfNotNull(property::setDescription, propertyDto.getDescription());
        updateIfNotNull(property::setSpecific, propertyDto.getSpecific());
        updateIfNotNull(property::setHilight, propertyDto.getHilight());
        updateIfNotNull(property::setArea, propertyDto.getArea());
        updateIfNotNull(property::setPrice, propertyDto.getPrice());
        updateIfNotNull(property::setFsp, propertyDto.getFsp());
        updateIfNotNull(property::setMapUrl, propertyDto.getMapUrl());
        updateIfNotNull(property::setLat, propertyDto.getLat());
        updateIfNotNull(property::setLng, propertyDto.getLng());
    }
}
