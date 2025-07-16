package com.otterdev.application.usecase.implUsecase.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.application.usecase.internal.base.InternalExpenseTypeUsecase;
import com.otterdev.domain.entity.ExpenseType;
import com.otterdev.domain.valueObject.dto.expenseType.ReqCreateExpenseTypeDto;
import com.otterdev.domain.valueObject.dto.expenseType.ReqUpdateExpenseTypeDto;
import com.otterdev.error_structure.UsecaseError;
import com.otterdev.infrastructure.service.internal.base.InternalExpenseTypeService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
class ExpenseTypeUsecaseImpl implements InternalExpenseTypeUsecase {

    private final InternalExpenseTypeService expenseTypeService;

    @Inject
    public ExpenseTypeUsecaseImpl(InternalExpenseTypeService expenseTypeService) {
        this.expenseTypeService = expenseTypeService;
    }

    

    @Override
    public Uni<Either<UsecaseError, ExpenseType>> createExpenseType(ReqCreateExpenseTypeDto reqCreateExpenseTypeDto,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createExpenseType'");
    }

    @Override
    public Uni<Either<UsecaseError, ExpenseType>> updateExpenseType(ReqUpdateExpenseTypeDto reqUpdateExpenseTypeDto,
            UUID expenseTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateExpenseType'");
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> deleteExpenseType(UUID expenseTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteExpenseType'");
    }

    @Override
    public Uni<Either<UsecaseError, ExpenseType>> getExpenseTypeById(UUID expenseTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getExpenseTypeById'");
    }

    @Override
    public Uni<Either<UsecaseError, List<ExpenseType>>> getAllExpenseTypes(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllExpenseTypes'");
    }
    
}
