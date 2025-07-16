package com.otterdev.infrastructure.service.internal.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.ExpenseType;
import com.otterdev.domain.valueObject.dto.expenseType.ReqCreateExpenseTypeDto;
import com.otterdev.domain.valueObject.dto.expenseType.ReqUpdateExpenseTypeDto;
import com.otterdev.error_structure.ServiceError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalExpenseTypeService {
    
    @WithTransaction
    Uni<Either<ServiceError, ExpenseType>> createExpenseType(ReqCreateExpenseTypeDto reqCreateExpenseTypeDto, UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, ExpenseType>> updateExpenseType(ReqUpdateExpenseTypeDto reqUpdateExpenseTypeDto, UUID expenseTypeId, UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, Boolean>> deleteExpenseType(UUID expenseTypeId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, ExpenseType>> getExpenseTypeById(UUID expenseTypeId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, List<ExpenseType>>> getAllExpenseTypes(UUID userId);

}
