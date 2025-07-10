package com.otterdev.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.otterdev.dto.entity.propertyStatus.ReqCreateUpdatePropertyStatusDto;
import com.otterdev.entity.table.PropertyStatus;
import com.otterdev.entity.table.User;
import com.otterdev.error.service.ResourceNotFoundError;
import com.otterdev.error.service.ServiceError;
import com.otterdev.error.service.UnexpectedError;
import com.otterdev.error.service.ValidationError;
import com.otterdev.repository.table.PropertyStatusRepository;
import com.otterdev.repository.table.UserRepository;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class PropertyStatusService {
    
    @Inject
    private PropertyStatusRepository propertyStatusRepository;

    @Inject
    private UserRepository userRepository;

    // new property status
    @WithTransaction
    public Uni<Either<ServiceError, PropertyStatus>> newPropertyStatus(ReqCreateUpdatePropertyStatusDto propertyStatusDto, UUID userId) {
        // Validate input
        if (propertyStatusDto == null || propertyStatusDto.getDetail() == null || propertyStatusDto.getDetail().trim().isEmpty()) {
            return Uni.createFrom().item(Either.left(new ValidationError("Invalid property status data: detail cannot be empty")));
        }
        
        // Check if the user already has this property status
        return propertyStatusRepository.existsByStatusDetailAndUserId(propertyStatusDto.getDetail().trim(), userId)
            .chain(exists -> {
                if (exists) {
                    return Uni.createFrom().item(Either.left(
                        new ValidationError("User already has a property status with this detail: " + propertyStatusDto.getDetail())
                    ));
                }
                return userRepository.findById(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, PropertyStatus>left(
                        new ResourceNotFoundError("User not found with ID: " + userId)
                    ));
                }
                
                User user = userOpt.get();
                LocalDateTime now = LocalDateTime.now();
                
                // Check if the user already has this property status
                

                // Create new PropertyStatus entity with proper User object and timestamps
                PropertyStatus newPropertyStatus = PropertyStatus.builder()
                    .detail(propertyStatusDto.getDetail().trim())
                    .createdBy(user)
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

                // Save the new PropertyStatus entity
                return propertyStatusRepository.persist(newPropertyStatus)
                    .chain(savedPropertyStatus -> Uni.createFrom().item(Either.right(savedPropertyStatus)));
            });
        });

        
        
    } // end new property status


    // edit property status
    @WithTransaction
    public Uni<Either<ServiceError, PropertyStatus>> editPropertyStatus(UUID propertyStatusId, ReqCreateUpdatePropertyStatusDto propertyStatusDto, UUID userId) {
        // Validate input
        if (propertyStatusDto == null || propertyStatusDto.getDetail() == null || propertyStatusDto.getDetail().trim().isEmpty()) {
            return Uni.createFrom().item(Either.left(new ValidationError("Invalid property status data: detail cannot be empty")));
        }
        
        return propertyStatusRepository.findById(propertyStatusId)
            .chain(propertyStatusOpt -> {
                if (propertyStatusOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.left(new ResourceNotFoundError("Property status not found with ID: " + propertyStatusId)));
                }

                PropertyStatus propertyStatus = propertyStatusOpt.get();
                propertyStatus.setDetail(propertyStatusDto.getDetail().trim());
                propertyStatus.setUpdatedAt(LocalDateTime.now());

                return propertyStatusRepository.persist(propertyStatus)
                    .chain(savedPropertyStatus -> Uni.createFrom().item(Either.right(savedPropertyStatus)));
            });
    } // end edit property status

    // view property status
    @WithTransaction
    public Uni<Either<ServiceError, Optional<PropertyStatus>>> viewPropertyStatusById(UUID propertyStatusId, UUID userId) {
        return propertyStatusRepository.findById(propertyStatusId)
            .map(propertyStatusOpt -> {
                if (propertyStatusOpt.isEmpty()) {
                    // Return Optional.empty() if not found
                    return Either.<ServiceError, Optional<PropertyStatus>>right(Optional.empty());
                }

                PropertyStatus propertyStatus = propertyStatusOpt.get();

                // Check if the user has permission to view
                if (!propertyStatus.getCreatedBy().getId().equals(userId)) {
                    return Either.<ServiceError, Optional<PropertyStatus>>left(
                        new ValidationError("You don't have permission to view this property status")
                    );
                }

                // Return the found property status wrapped in Optional
                return Either.<ServiceError, Optional<PropertyStatus>>right(Optional.of(propertyStatus));
            })
            .onFailure().recoverWithItem(throwable -> 
                // Handle database errors
                Either.<ServiceError, Optional<PropertyStatus>>left(
                    new UnexpectedError("Failed to retrieve property status: " + throwable.getMessage(), throwable)
                )
            );
    } // end view property status

    // view all user property status
    @WithTransaction
    public Uni<Either<ServiceError, List<PropertyStatus>>> viewAllUserPropertyStatus(UUID userId) {
        return propertyStatusRepository.findByCreatedById(userId)
            .map(propertyStatuses -> Either.<ServiceError, List<PropertyStatus>>right(propertyStatuses))
            .onFailure().recoverWithItem(throwable -> 
                Either.<ServiceError, List<PropertyStatus>>left(
                    new UnexpectedError("Failed to retrieve property statuses: " + throwable.getMessage(), throwable)
                )
            );
    } // end view all user property status

    // remove property status
    public Uni<Either<ServiceError, Boolean>> removePropertyStatus(UUID propertyStatusId, UUID userId) {
        return propertyStatusRepository.findById(propertyStatusId)
            .chain(propertyStatusOpt -> {
                if (propertyStatusOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, Boolean>left(
                        new ResourceNotFoundError("Property status not found with ID: " + propertyStatusId)
                    ));
                }

                PropertyStatus propertyStatus = propertyStatusOpt.get();

                // Check if user has permission to delete (only creator can delete)
                if (!propertyStatus.getCreatedBy().getId().equals(userId)) {
                    return Uni.createFrom().item(Either.<ServiceError, Boolean>left(
                        new ValidationError("You don't have permission to delete this property status")
                    ));
                }

                return propertyStatusRepository.delete(propertyStatus)
                    .map(deleted -> Either.<ServiceError, Boolean>right(true))
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, Boolean>left(
                            new UnexpectedError("Failed to delete property status: " + throwable.getMessage(), throwable)
                        )
                    );
            });
    } // end remove property status
}
