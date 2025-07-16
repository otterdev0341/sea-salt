package com.otterdev.application.usecase.internal.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.Expense;
import com.otterdev.domain.valueObject.dto.expense.ReqCreateExpenseDto;
import com.otterdev.domain.valueObject.dto.expense.ReqUpdateExpenseDto;
import com.otterdev.error_structure.UsecaseError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalExpenseUsecase {
    
    @WithTransaction
    Uni<Either<UsecaseError, Expense>> createExpense(ReqCreateExpenseDto reqCreateExpenseDto, UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, Expense>> updateExpense(UUID expenseId, ReqUpdateExpenseDto reqUpdateExpenseDto, UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> deleteExpense(UUID expenseId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, Expense>> getExpense(UUID expenseId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, List<Expense>>> getAllExpenses(UUID userId);
    
    @WithSession
    Uni<Either<UsecaseError, List<Expense>>> getAllExpenseByExpenseType(UUID userId, UUID expenseTypeId);
}
