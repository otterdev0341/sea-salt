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
        
        return expenseTypeService.createExpenseType(reqCreateExpenseTypeDto, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to create expense type cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));
    }

    @Override
    public Uni<Either<UsecaseError, ExpenseType>> updateExpenseType(ReqUpdateExpenseTypeDto reqUpdateExpenseTypeDto,
            UUID expenseTypeId, UUID userId) {
        
                return expenseTypeService.updateExpenseType(reqUpdateExpenseTypeDto, expenseTypeId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to update expense type cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> deleteExpenseType(UUID expenseTypeId, UUID userId) {
        
        return expenseTypeService.deleteExpenseType(expenseTypeId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to delete expense type cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));
    }

    @Override
    public Uni<Either<UsecaseError, ExpenseType>> getExpenseTypeById(UUID expenseTypeId, UUID userId) {
        
        return expenseTypeService.getExpenseTypeById(expenseTypeId, userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to retrieve expense type cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));
    }

    @Override
    public Uni<Either<UsecaseError, List<ExpenseType>>> getAllExpenseTypes(UUID userId) {
        
        return expenseTypeService.getAllExpenseTypes(userId)
                .chain(result -> result.fold(
                    error -> Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to retrieve expense types cause by : " + error.message()))), 
                    success -> Uni.createFrom().item(Either.right(success))
                ));
    }
    
}
