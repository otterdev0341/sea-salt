package com.otterdev.infrastructure.service.implService.base;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.Expense;
import com.otterdev.domain.valueObject.dto.expense.ReqCreateExpenseDto;
import com.otterdev.domain.valueObject.dto.expense.ReqUpdateExpenseDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.repository.ExpenseRepository;
import com.otterdev.infrastructure.repository.ExpenseTypeRepository;
import com.otterdev.infrastructure.repository.UserRepository;
import com.otterdev.infrastructure.service.internal.base.InternalExpenseService;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@ApplicationScoped
@Named("expenseService")  // Add this qualifier
class ExpenseServiceImpl implements InternalExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseTypeRepository expenseTypeRepository;
    private final UserRepository userRepository;

    @Inject
    public ExpenseServiceImpl(ExpenseRepository expenseRepository, ExpenseTypeRepository expenseTypeRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseTypeRepository = expenseTypeRepository;
        this.userRepository = userRepository;
    }
    

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, Expense>> createExpense(ReqCreateExpenseDto reqCreateExpenseDto, UUID userId) {
        
        Expense newExpense = new Expense();
        LocalDateTime now = LocalDateTime.now();

        return expenseTypeRepository.findByIdAndUserId(reqCreateExpenseDto.getExpenseType(), userId)
            .chain(expenseTypeOpt -> {
                if (expenseTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.left(new ServiceError.NotFound("Expense type not found")));
                }
                return userRepository.findByUserId(userId)
                    .chain(userOpt -> {
                        if (userOpt.isEmpty()) {
                            return Uni.createFrom().item(Either.left(new ServiceError.NotFound("User not found")));
                        }
                        newExpense.setExpenseType(expenseTypeOpt.get());
                        newExpense.setDetail(reqCreateExpenseDto.getDetail().trim());
                        newExpense.setCreatedBy(userOpt.get());
                        newExpense.setCreatedAt(now);
                        newExpense.setUpdatedAt(now);
                        return expenseRepository.persist(newExpense)
                            .replaceWith(Either.right(newExpense));
                    });
            });

    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, Expense>> updateExpense(UUID expenseId, ReqUpdateExpenseDto reqUpdateExpenseDto,
            UUID userId) {
        
        // check expense type in dto is exist
        // check user id
        // get contact and check is it belong to user
        // update expense and return
        return expenseTypeRepository.findByIdAndUserId(reqUpdateExpenseDto.getExpenseType(), userId)
            .chain(expenseTypeOpt -> {
                if (expenseTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.left(new ServiceError.NotFound("Expense type not found")));
                }
                return userRepository.findByUserId(userId)
                    .chain(userOpt -> {
                        if (userOpt.isEmpty()) {
                            return Uni.createFrom().item(Either.left(new ServiceError.NotFound("User not found")));
                        }
                        
                        return expenseRepository.findByIdAndUserId(expenseId, userId)
                            .chain(expenseOpt -> {
                                if (expenseOpt.isEmpty()) {
                                    return Uni.createFrom().item(Either.left(new ServiceError.NotFound("Expense not found")));
                                }
                                Expense expense = expenseOpt.get();
                                expense.setDetail(reqUpdateExpenseDto.getDetail().trim());
                                expense.setExpenseType(expenseTypeOpt.get());
                                expense.setCreatedBy(userOpt.get());
                                expense.setUpdatedAt(LocalDateTime.now());
                                
                                return expenseRepository.updateExpense(expense, userId)
                                    .chain(updatedExpense -> updatedExpense.fold(
                                        error -> Uni.createFrom().item(Either.left(new ServiceError.OperationFailed("Failed to update expense: " + error.message()))),
                                        success -> Uni.createFrom().item(Either.right(success))
                                    ));
                            });
                    });
            });

    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, Boolean>> deleteExpense(UUID expenseId, UUID userId) {
        
        return expenseRepository.deleteExpense(expenseId, userId)
            .chain(result -> result.fold(
                error -> Uni.createFrom().item(Either.left(new ServiceError.OperationFailed("Failed to delete expense: " + error.message()))),
                success -> Uni.createFrom().item(Either.right(success))
            ));

    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, Expense>> getExpense(UUID expenseId, UUID userId) {
        
        return expenseRepository.findByIdAndUserId(expenseId, userId)
            .chain(expenseOpt -> {
                if (expenseOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.left(new ServiceError.NotFound("Expense not found")));
                }
                return Uni.createFrom().item(Either.right(expenseOpt.get()));
            });

    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, List<Expense>>> getAllExpenses(UUID userId) {
        
        return expenseRepository.findAllByUserId(userId)
            .chain(result -> result.fold(
                error -> Uni.createFrom().item(Either.left(new ServiceError.OperationFailed("Failed to retrieve expenses: " + error.message()))),
                expenses -> Uni.createFrom().item(Either.right(expenses))
            ));

    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, List<Expense>>> getAllExpenseByExpenseType(UUID userId, UUID expenseTypeId) {
        
        return expenseTypeRepository.findByIdAndUserId(expenseTypeId, userId)
            .chain(expenseTypeOpt -> {
                if (expenseTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(Either.left(new ServiceError.NotFound("Expense type not found")));
                }
                return expenseRepository.findByExpenseTypeAndUserId(expenseTypeOpt.get(), userId)
                    .chain(result -> result.fold(
                        error -> Uni.createFrom().item(Either.left(new ServiceError.OperationFailed("Failed to retrieve expenses by expense type: " + error.message()))),
                        expenses -> Uni.createFrom().item(Either.right(expenses))
                    ));
            });

    }
    
}
