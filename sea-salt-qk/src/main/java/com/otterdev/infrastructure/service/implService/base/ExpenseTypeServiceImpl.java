package com.otterdev.infrastructure.service.implService.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.ExpenseType;
import com.otterdev.domain.valueObject.dto.expenseType.ReqCreateExpenseTypeDto;
import com.otterdev.domain.valueObject.dto.expenseType.ReqUpdateExpenseTypeDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.repository.ExpenseTypeRepository;
import com.otterdev.infrastructure.service.internal.base.InternalExpenseTypeService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
class ExpenseTypeServiceImpl implements InternalExpenseTypeService {

    private final ExpenseTypeRepository expenseTypeRepository;

    @Inject
    public ExpenseTypeServiceImpl(ExpenseTypeRepository expenseTypeRepository) {
        this.expenseTypeRepository = expenseTypeRepository;
    }


    @Override
    public Uni<Either<ServiceError, ExpenseType>> createExpenseType(ReqCreateExpenseTypeDto reqCreateExpenseTypeDto,
            UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createExpenseType'");
    }

    @Override
    public Uni<Either<ServiceError, ExpenseType>> updateExpenseType(ReqUpdateExpenseTypeDto reqUpdateExpenseTypeDto,
            UUID expenseTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateExpenseType'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> deleteExpenseType(UUID expenseTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteExpenseType'");
    }

    @Override
    public Uni<Either<ServiceError, ExpenseType>> getExpenseTypeById(UUID expenseTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getExpenseTypeById'");
    }

    @Override
    public Uni<Either<ServiceError, List<ExpenseType>>> getAllExpenseTypes(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllExpenseTypes'");
    }
    
}
