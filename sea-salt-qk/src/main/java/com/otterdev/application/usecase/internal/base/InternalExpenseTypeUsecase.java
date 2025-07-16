package com.otterdev.application.usecase.internal.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.ExpenseType;
import com.otterdev.domain.valueObject.dto.expenseType.ReqCreateExpenseTypeDto;
import com.otterdev.domain.valueObject.dto.expenseType.ReqUpdateExpenseTypeDto;
import com.otterdev.error_structure.UsecaseError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalExpenseTypeUsecase {
    
    @WithTransaction
    Uni<Either<UsecaseError, ExpenseType>> createExpenseType(ReqCreateExpenseTypeDto reqCreateExpenseTypeDto, UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, ExpenseType>> updateExpenseType(ReqUpdateExpenseTypeDto reqUpdateExpenseTypeDto, UUID expenseTypeId, UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> deleteExpenseType(UUID expenseTypeId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, ExpenseType>> getExpenseTypeById(UUID expenseTypeId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, List<ExpenseType>>> getAllExpenseTypes(UUID userId);

}
