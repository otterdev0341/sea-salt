package com.otterdev.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.otterdev.dto.entity.expenseType.ReqCreateUpdateExpenseTypeDto;
import com.otterdev.entity.table.ExpenseType;
import com.otterdev.entity.table.User;
import com.otterdev.error.service.ResourceNotFoundError;
import com.otterdev.error.service.ServiceError;
import com.otterdev.error.service.UnexpectedError;
import com.otterdev.error.service.ValidationError;
import com.otterdev.repository.table.ExpenseTypeRepository;
import com.otterdev.repository.table.UserRepository;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class ExpenseTypeService {
    
    @Inject
    private UserRepository userRepository;

    @Inject
    private ExpenseTypeRepository expenseTypeRepository;


    // new ExpenseType method
    @WithTransaction
    public Uni<Either<ServiceError, ExpenseType>> newExpenseType(ReqCreateUpdateExpenseTypeDto expenseTypeDto, UUID userId) {
        // Validate input
        if (expenseTypeDto == null || expenseTypeDto.getDetail() == null || expenseTypeDto.getDetail().trim().isEmpty()) {
            return Uni.createFrom().item(Either.left(new ValidationError("Invalid expense type data: description cannot be empty")));
        }

        // Fetch the User entity to ensure it exists
        return userRepository.findById(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, ExpenseType>left(
                        new ResourceNotFoundError("User not found with ID: " + userId)
                    ));
                }

                User user = userOpt.get();
                LocalDateTime now = LocalDateTime.now();

                // Check if an ExpenseType with the same detail already exists for this user
                return expenseTypeRepository.existsByDetailAndCreatedBy(expenseTypeDto.getDetail().trim(), user.getId())
                    .chain(exists -> {
                        if (exists) {
                            return Uni.createFrom().item(Either.<ServiceError, ExpenseType>left(
                                new ValidationError("Expense type with detail '" + expenseTypeDto.getDetail().trim() + "' already exists for this user")
                            ));
                        }

                        // Create new ExpenseType entity
                        ExpenseType newExpenseType = ExpenseType.builder()
                            .detail(expenseTypeDto.getDetail().trim())
                            .createdBy(user)
                            .createdAt(now)
                            .updatedAt(now)
                            .build();

                        // Save the new ExpenseType entity
                        return expenseTypeRepository.persist(newExpenseType)
                            .map(savedExpenseType -> Either.<ServiceError, ExpenseType>right(savedExpenseType))
                            .onFailure().recoverWithItem(throwable -> 
                                Either.<ServiceError, ExpenseType>left(
                                    new UnexpectedError("Failed to create expense type: " + throwable.getMessage(), throwable)
                                )
                            );
                    });
            })
            .onFailure().recoverWithItem(throwable -> 
                Either.<ServiceError, ExpenseType>left(
                    new UnexpectedError("Failed to create expense type", throwable)
                )
            );
    }

    // edit ExpenseType method

    @WithTransaction
    public Uni<Either<ServiceError, ExpenseType>> editExpenseType(UUID expenseTypeId, ReqCreateUpdateExpenseTypeDto expenseTypeDto, UUID userId) {
        // Validate input
        if (expenseTypeDto == null || expenseTypeDto.getDetail() == null || expenseTypeDto.getDetail().trim().isEmpty()) {
            return Uni.createFrom().item(Either.left(new ValidationError("Invalid expense type data: detail cannot be empty")));
        }

        return expenseTypeRepository.findById(expenseTypeId)
            .chain(expenseTypeOpt -> {
                if (expenseTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, ExpenseType>left(
                        new ResourceNotFoundError("Expense type not found with ID: " + expenseTypeId)
                    ));
                }

                ExpenseType expenseType = expenseTypeOpt.get();

                // Check if user has permission to update (only creator can update)
                if (!expenseType.getCreatedBy().getId().equals(userId)) {
                    return Uni.createFrom().item(Either.<ServiceError, ExpenseType>left(
                        new ValidationError("User does not have permission to update this expense type")
                    ));
                }

                // Check if an ExpenseType with the same detail already exists for this user
                return expenseTypeRepository.existsByDetailAndCreatedBy(expenseTypeDto.getDetail().trim(), userId)
                    .chain(exists -> {
                        if (exists) {
                            return Uni.createFrom().item(Either.<ServiceError, ExpenseType>left(
                                new ValidationError("Expense type with detail '" + expenseTypeDto.getDetail().trim() + "' already exists for this user")
                            ));
                        }

                        // Update the existing ExpenseType entity
                        expenseType.setDetail(expenseTypeDto.getDetail().trim());
                        LocalDateTime now = LocalDateTime.now();
                        expenseType.setUpdatedAt(now);

                        // Save the updated ExpenseType entity
                        return expenseTypeRepository.persist(expenseType)
                            .map(updatedExpenseType -> Either.<ServiceError, ExpenseType>right(updatedExpenseType))
                            .onFailure().recoverWithItem(throwable -> 
                                Either.<ServiceError, ExpenseType>left(
                                    new UnexpectedError("Failed to update expense type: " + throwable.getMessage(), throwable)
                                )
                            );
                    });
            })
            .onFailure().recoverWithItem(throwable -> 
                Either.<ServiceError, ExpenseType>left(
                    new UnexpectedError("Failed to update expense type", throwable)
                )
            );
    } // end editExpenseType method

    // view ExpenseType method
    @WithSession
    public Uni<Either<ServiceError, Optional<ExpenseType>>> viewExpenseTypeById(UUID expenseTypeId, UUID userId) {
        return expenseTypeRepository.findById(expenseTypeId)
            .map(expenseTypeOpt -> {
                if (expenseTypeOpt.isEmpty()) {
                    // Return ResourceNotFoundError if the expense type is not found
                    return Either.<ServiceError, Optional<ExpenseType>>left(
                        new ResourceNotFoundError("Expense type not found with ID: " + expenseTypeId)
                    );
                }

                ExpenseType expenseType = expenseTypeOpt.get();

                // Check if the user has permission to view (only the creator can view)
                if (!expenseType.getCreatedBy().getId().equals(userId)) {
                    return Either.<ServiceError, Optional<ExpenseType>>left(
                        new ValidationError("You don't have permission to view this expense type")
                    );
                }

                // Return the found ExpenseType wrapped in Optional
                return Either.<ServiceError, Optional<ExpenseType>>right(Optional.of(expenseType));
            })
            .onFailure().recoverWithItem(throwable -> 
                // Handle database errors
                Either.<ServiceError, Optional<ExpenseType>>left(
                    new UnexpectedError("Failed to retrieve expense type: " + throwable.getMessage(), throwable)
                )
            );
    }
        
    // view all ExpenseType method
    @WithSession
    public Uni<Either<ServiceError, List<ExpenseType>>> viewAllUserExpenseTypes(UUID userId) {
        return expenseTypeRepository.findAllByUserId(userId)
            .map(expenseTypes -> {
                if (expenseTypes.isEmpty()) {
                    // Return ResourceNotFoundError if no expense types are found
                    return Either.<ServiceError, List<ExpenseType>>left(
                        new ResourceNotFoundError("No expense types found for user with ID: " + userId)
                    );
                }
                // Return the list of ExpenseTypes
                return Either.<ServiceError, List<ExpenseType>>right(expenseTypes);
            })
            .onFailure().recoverWithItem(throwable -> 
                // Handle database errors
                Either.<ServiceError, List<ExpenseType>>left(
                    new UnexpectedError("Failed to retrieve expense types: " + throwable.getMessage(), throwable)
                )
            );
    }

    // delete ExpenseType method
    @WithTransaction
    public Uni<Either<ServiceError, Boolean>> deleteExpenseType(UUID expenseTypeId, UUID userId) {
        return expenseTypeRepository.findById(expenseTypeId)
            .chain(expenseTypeOpt -> {
                if (expenseTypeOpt.isEmpty()) {
                    // Return ResourceNotFoundError if the expense type is not found
                    return Uni.createFrom().item(Either.<ServiceError, Boolean>left(
                        new ResourceNotFoundError("Expense type not found with ID: " + expenseTypeId)
                    ));
                }

                ExpenseType expenseType = expenseTypeOpt.get();

                // Check if the user has permission to delete (only the creator can delete)
                if (!expenseType.getCreatedBy().getId().equals(userId)) {
                    return Uni.createFrom().item(Either.<ServiceError, Boolean>left(
                        new ValidationError("You don't have permission to delete this expense type")
                    ));
                }

                // Delete the expense type
                return expenseTypeRepository.delete(expenseType)
                    .map(deleted -> Either.<ServiceError, Boolean>right(true))
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, Boolean>left(
                            new UnexpectedError("Failed to delete expense type: " + throwable.getMessage(), throwable)
                        )
                    );
            });
    }
        

} // end class
