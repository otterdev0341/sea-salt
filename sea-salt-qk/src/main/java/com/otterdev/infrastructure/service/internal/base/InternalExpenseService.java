package com.otterdev.infrastructure.service.internal.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.Expense;
import com.otterdev.domain.valueObject.dto.expense.ReqCreateExpenseDto;
import com.otterdev.domain.valueObject.dto.expense.ReqUpdateExpenseDto;
import com.otterdev.error_structure.ServiceError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalExpenseService {
    
    @WithTransaction
    Uni<Either<ServiceError, Expense>> createExpense(ReqCreateExpenseDto reqCreateExpenseDto, UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, Expense>> updateExpense(UUID expenseId, ReqUpdateExpenseDto reqUpdateExpenseDto, UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, Boolean>> deleteExpense(UUID expenseId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, Expense>> getExpense(UUID expenseId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, List<Expense>>> getAllExpenses(UUID userId);
    
    @WithSession
    Uni<Either<ServiceError, List<Expense>>> getAllExpenseByExpenseType(UUID userId, UUID expenseTypeId);
}
