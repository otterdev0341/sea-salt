package com.otterdev.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.otterdev.dto.entity.expense.ReqCreateUpdateExpenseDto;
import com.otterdev.entity.table.Expense;
import com.otterdev.entity.table.ExpenseType;
import com.otterdev.entity.table.User;
import com.otterdev.error.service.ResourceNotFoundError;
import com.otterdev.error.service.ServiceError;
import com.otterdev.error.service.ValidationError;
import com.otterdev.error.service.UnexpectedError;
import com.otterdev.repository.table.ExpenseRepository;
import com.otterdev.repository.table.ExpenseTypeRepository;
import com.otterdev.repository.table.UserRepository;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ExpenseService {

    @Inject
    UserRepository userRepository;

    @Inject
    ExpenseRepository expenseRepository;

    @Inject
    ExpenseTypeRepository expenseTypeRepository;

    // Create a new Expense
    @WithTransaction
    public Uni<Either<ServiceError, Expense>> newExpense(ReqCreateUpdateExpenseDto expenseDto, UUID userId) {
        if (expenseDto == null || expenseDto.getExpenseType() == null) {
            return Uni.createFrom().item(Either.left(new ValidationError("Expense data or expense type ID cannot be null")));
        }

        return userRepository.findById(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, Expense>left(
                        new ResourceNotFoundError("User not found with ID: " + userId)
                    ));
                }

                User user = userOpt.get();

                return expenseTypeRepository.findById(expenseDto.getExpenseType())
                    .chain(expenseTypeOpt -> {
                        if (expenseTypeOpt.isEmpty()) {
                            return Uni.createFrom().item(Either.<ServiceError, Expense>left(
                                new ResourceNotFoundError("Expense type not found with ID: " + expenseDto.getExpenseType())
                            ));
                        }

                        ExpenseType expenseType = expenseTypeOpt.get();

                        if (!expenseType.getCreatedBy().getId().equals(userId)) {
                            return Uni.createFrom().item(Either.<ServiceError, Expense>left(
                                new ValidationError("You don't have permission to use this expense type")
                            ));
                        }

                        LocalDateTime now = LocalDateTime.now();

                        Expense newExpense = new Expense();
                        
                        newExpense.setDetail(expenseDto.getDetail());
                        newExpense.setExpenseType(expenseDto.getExpenseType());
                        newExpense.setCreatedBy(user);
                        newExpense.setCreatedAt(now);
                        newExpense.setUpdatedAt(now);

                        return expenseRepository.persist(newExpense)
                            .map(savedExpense -> Either.<ServiceError, Expense>right(savedExpense))
                            .onFailure().recoverWithItem(throwable -> 
                                Either.<ServiceError, Expense>left(
                                    new UnexpectedError("Failed to create expense: " + throwable.getMessage(), throwable)
                                )
                            );
                    });
            });
    }

    // Edit an existing Expense
    @WithTransaction
    public Uni<Either<ServiceError, Expense>> editExpense(UUID expenseId, ReqCreateUpdateExpenseDto expenseDto, UUID userId) {
        if (expenseDto == null || expenseDto.getExpenseType() == null) {
            return Uni.createFrom().item(Either.left(new ValidationError("Expense data or expense type ID cannot be null")));
        }

        return expenseRepository.findById(expenseId)
            .chain(expenseOpt -> {
                if (expenseOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, Expense>left(
                        new ResourceNotFoundError("Expense not found with ID: " + expenseId)
                    ));
                }

                Expense expense = expenseOpt.get();

                if (!expense.getCreatedBy().getId().equals(userId)) {
                    return Uni.createFrom().item(Either.<ServiceError, Expense>left(
                        new ValidationError("You don't have permission to update this expense")
                    ));
                }

                return expenseTypeRepository.findById(expenseDto.getExpenseType())
                    .chain(expenseTypeOpt -> {
                        if (expenseTypeOpt.isEmpty()) {
                            return Uni.createFrom().item(Either.<ServiceError, Expense>left(
                                new ResourceNotFoundError("Expense type not found with ID: " + expenseDto.getExpenseType())
                            ));
                        }

                        ExpenseType expenseType = expenseTypeOpt.get();

                        if (!expenseType.getCreatedBy().getId().equals(userId)) {
                            return Uni.createFrom().item(Either.<ServiceError, Expense>left(
                                new ValidationError("You don't have permission to use this expense type")
                            ));
                        }

                        
                        expense.setDetail(expenseDto.getDetail());
                        expense.setExpenseType(expenseDto.getExpenseType());
                        expense.setUpdatedAt(LocalDateTime.now());

                        return expenseRepository.persist(expense)
                            .map(updatedExpense -> Either.<ServiceError, Expense>right(updatedExpense))
                            .onFailure().recoverWithItem(throwable -> 
                                Either.<ServiceError, Expense>left(
                                    new UnexpectedError("Failed to update expense: " + throwable.getMessage(), throwable)
                                )
                            );
                    });
            });
    }

    // Delete an Expense
    @WithTransaction
    public Uni<Either<ServiceError, Boolean>> deleteExpense(UUID expenseId, UUID userId) {
        return expenseRepository.findById(expenseId)
            .chain(expenseOpt -> {
                if (expenseOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, Boolean>left(
                        new ResourceNotFoundError("Expense not found with ID: " + expenseId)
                    ));
                }

                Expense expense = expenseOpt.get();

                if (!expense.getCreatedBy().getId().equals(userId)) {
                    return Uni.createFrom().item(Either.<ServiceError, Boolean>left(
                        new ValidationError("You don't have permission to delete this expense")
                    ));
                }

                return expenseRepository.delete(expense)
                    .map(deleted -> Either.<ServiceError, Boolean>right(true))
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, Boolean>left(
                            new UnexpectedError("Failed to delete expense: " + throwable.getMessage(), throwable)
                        )
                    );
            });
    }

    // View an Expense by ID
    @WithSession
    public Uni<Either<ServiceError, Expense>> viewExpenseById(UUID expenseId, UUID userId) {
        return expenseRepository.findById(expenseId)
            .map(expenseOpt -> {
                if (expenseOpt.isEmpty()) {
                    return Either.<ServiceError, Expense>left(
                        new ResourceNotFoundError("Expense not found with ID: " + expenseId)
                    );
                }

                Expense expense = expenseOpt.get();

                if (!expense.getCreatedBy().getId().equals(userId)) {
                    return Either.<ServiceError, Expense>left(
                        new ValidationError("You don't have permission to view this expense")
                    );
                }

                return Either.<ServiceError, Expense>right(expense);
            })
            .onFailure().recoverWithItem(throwable -> 
                Either.<ServiceError, Expense>left(
                    new UnexpectedError("Failed to retrieve expense: " + throwable.getMessage(), throwable)
                )
            );
    }

    // View all Expenses for a User
    @WithSession
    public Uni<Either<ServiceError, List<Expense>>> viewAllUserExpenses(UUID userId) {
        return userRepository.findById(userId)
            .chain(userOpt -> {
                if (userOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.<ServiceError, List<Expense>>left(
                        new ResourceNotFoundError("User not found with ID: " + userId)
                    ));
                }

                User user = userOpt.get();
                return expenseRepository.find("createdBy", user).list()
                    .map(expenses -> Either.<ServiceError, List<Expense>>right(expenses))
                    .onFailure().recoverWithItem(throwable -> 
                        Either.<ServiceError, List<Expense>>left(
                            new UnexpectedError("Failed to retrieve user expenses: " + throwable.getMessage(), throwable)
                        )
                    );
            });
    }
}
