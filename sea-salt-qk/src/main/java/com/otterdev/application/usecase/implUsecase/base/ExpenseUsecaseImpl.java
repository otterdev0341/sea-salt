package com.otterdev.application.usecase.implUsecase.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.application.usecase.internal.base.InternalExpenseUsecase;
import com.otterdev.domain.entity.Expense;
import com.otterdev.domain.valueObject.dto.expense.ReqCreateExpenseDto;
import com.otterdev.domain.valueObject.dto.expense.ReqUpdateExpenseDto;
import com.otterdev.error_structure.UsecaseError;
import com.otterdev.infrastructure.service.internal.base.InternalExpenseService;
import com.spencerwi.either.Either;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
class ExpenseUsecaseImpl implements InternalExpenseUsecase{

    private final InternalExpenseService expenseService;

    @Inject
    public ExpenseUsecaseImpl(InternalExpenseService expenseService) {
        this.expenseService = expenseService;
    }


    @Override
    public Uni<Either<UsecaseError, Expense>> createExpense(ReqCreateExpenseDto reqCreateExpenseDto, UUID userId) {
        
        return expenseService.createExpense(reqCreateExpenseDto, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to create expense cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(result.getRight()))
                ));

    }

    @Override
    public Uni<Either<UsecaseError, Expense>> updateExpense(UUID expenseId, ReqUpdateExpenseDto reqUpdateExpenseDto,
            UUID userId) {
        
        return expenseService.updateExpense(expenseId, reqUpdateExpenseDto, userId)
            .chain(result -> result.fold(
                error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to update expense cause by : " + error.message()))), 
                success -> Uni.createFrom().item(Either.right(result.getRight()))
            ));

    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> deleteExpense(UUID expenseId, UUID userId) {
        
        return expenseService.deleteExpense(expenseId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to delete expense cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));

    }

    @Override
    public Uni<Either<UsecaseError, Expense>> getExpense(UUID expenseId, UUID userId) {
        
        return expenseService.getExpense(expenseId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to retrieve expense cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));

    }

    @Override
    public Uni<Either<UsecaseError, List<Expense>>> getAllExpenses(UUID userId) {
    
        return expenseService.getAllExpenses(userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to retrieve expenses cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));

    }

    @Override
    public Uni<Either<UsecaseError, List<Expense>>> getAllExpenseByExpenseType(UUID userId, UUID expenseTypeId) {
        
        return expenseService.getAllExpenseByExpenseType(userId, expenseTypeId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to retrieve expenses by type cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));

    }
    
}
