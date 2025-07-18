package com.otterdev.infrastructure.service.implService.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.Expense;
import com.otterdev.domain.valueObject.dto.expense.ReqCreateExpenseDto;
import com.otterdev.domain.valueObject.dto.expense.ReqUpdateExpenseDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.repository.ExpenseRepository;
import com.otterdev.infrastructure.service.internal.base.InternalExpenseService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@ApplicationScoped
@Named("expenseService")  // Add this qualifier
class ExpenseServiceImpl implements InternalExpenseService {

    private final ExpenseRepository expenseRepository;

    @Inject
    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Uni<Either<ServiceError, Expense>> createExpense(ReqCreateExpenseDto reqCreateExpenseDto, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createExpense'");
    }

    @Override
    public Uni<Either<ServiceError, Expense>> updateExpense(UUID expenseId, ReqUpdateExpenseDto reqUpdateExpenseDto,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateExpense'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> deleteExpense(UUID expenseId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteExpense'");
    }

    @Override
    public Uni<Either<ServiceError, Expense>> getExpense(UUID expenseId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getExpense'");
    }

    @Override
    public Uni<Either<ServiceError, List<Expense>>> getAllExpenses(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllExpenses'");
    }

    @Override
    public Uni<Either<ServiceError, List<Expense>>> getAllExpenseByExpenseType(UUID userId, UUID expenseTypeId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllExpenseByExpenseType'");
    }
    
}
