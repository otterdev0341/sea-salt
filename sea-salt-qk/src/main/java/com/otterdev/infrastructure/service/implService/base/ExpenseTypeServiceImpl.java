package com.otterdev.infrastructure.service.implService.base;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.ExpenseType;
import com.otterdev.domain.valueObject.dto.expenseType.ReqCreateExpenseTypeDto;
import com.otterdev.domain.valueObject.dto.expenseType.ReqUpdateExpenseTypeDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.repository.ExpenseTypeRepository;
import com.otterdev.infrastructure.repository.UserRepository;
import com.otterdev.infrastructure.service.internal.base.InternalExpenseTypeService;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
@Named("expenseTypeService")
class ExpenseTypeServiceImpl implements InternalExpenseTypeService {

    private final ExpenseTypeRepository expenseTypeRepository;
    private final UserRepository userRepository;

    @Inject
    public ExpenseTypeServiceImpl(ExpenseTypeRepository expenseTypeRepository, UserRepository userRepository) {
        this.expenseTypeRepository = expenseTypeRepository;
        this.userRepository = userRepository;
    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, ExpenseType>> createExpenseType(
            ReqCreateExpenseTypeDto reqCreateExpenseTypeDto, 
            UUID userId) {
        
        ExpenseType newExpenseType = new ExpenseType();
        LocalDateTime now = LocalDateTime.now();

        return expenseTypeRepository.isExistByDetailAndUserId(reqCreateExpenseTypeDto.getDetail().trim(), userId)
            .chain(isExist -> {
                if (isExist) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.DuplicateEntry("Expense type already exists"))
                    );
                }
                return userRepository.findByUserId(userId)
                    .chain(userOpt -> {
                        if (userOpt.isEmpty()) {
                            return Uni.createFrom().item(
                                Either.left(new ServiceError.NotFound("User not found"))
                            );
                        }
                        newExpenseType.setDetail(reqCreateExpenseTypeDto.getDetail().trim());
                        newExpenseType.setCreatedBy(userOpt.get());
                        newExpenseType.setCreatedAt(now);
                        newExpenseType.setUpdatedAt(now);

                        return expenseTypeRepository.persist(newExpenseType)
                            .map(persistedExpenseType -> Either.right(persistedExpenseType));
                    });
            });
    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, ExpenseType>> updateExpenseType(
            ReqUpdateExpenseTypeDto reqUpdateExpenseTypeDto,
            UUID expenseTypeId, 
            UUID userId) {
        
        return expenseTypeRepository.findByIdAndUserId(expenseTypeId, userId)
            .chain(expenseTypeOpt -> {
                if (expenseTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.NotFound("Expense type not found"))
                    );
                }

                ExpenseType existingExpenseType = expenseTypeOpt.get();
                String newDetail = reqUpdateExpenseTypeDto.getDetail().trim();

                if (newDetail.equals(existingExpenseType.getDetail())) {
                    return Uni.createFrom().item(Either.right(existingExpenseType));
                }

                return expenseTypeRepository.isExistByDetailAndUserId(newDetail, userId)
                    .chain(exists -> {
                        if (exists) {
                            return Uni.createFrom().item(
                                Either.left(new ServiceError.DuplicateEntry("Expense type with this detail already exists"))
                            );
                        }

                        existingExpenseType.setDetail(newDetail);
                        existingExpenseType.setUpdatedAt(LocalDateTime.now());

                        return expenseTypeRepository.updateExpenseType(existingExpenseType, userId)
                            .chain(updatedResult -> {
                                if (updatedResult.isLeft()) {
                                    return Uni.createFrom().item(
                                        Either.left(new ServiceError.OperationFailed(
                                            "Failed to update expense type: " + updatedResult.getLeft().message()
                                        ))
                                    );
                                }
                                return expenseTypeRepository.findByIdAndUserId(existingExpenseType.getId(), userId)
                                    .map(expenseType -> Either.right(expenseType.orElse(null)));
                            });
                    });
            });
    }

    @Override
    @WithTransaction
    public Uni<Either<ServiceError, Boolean>> deleteExpenseType(UUID expenseTypeId, UUID userId) {
        return expenseTypeRepository.findByIdAndUserId(expenseTypeId, userId)
            .chain(expenseTypeOpt -> {
                if (expenseTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.NotFound("Expense type not found"))
                    );
                }

                return expenseTypeRepository.deleteExpenseType(expenseTypeId, userId)
                    .chain(result -> {
                        if (result.isLeft()) {
                            return Uni.createFrom().item(
                                Either.left(new ServiceError.OperationFailed(
                                    "Failed to delete expense type with id: " + expenseTypeId
                                ))
                            );
                        }
                        return Uni.createFrom().item(Either.right(true));
                    });
            });
    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, ExpenseType>> getExpenseTypeById(UUID expenseTypeId, UUID userId) {
        return expenseTypeRepository.findByIdAndUserId(expenseTypeId, userId)
            .chain(expenseTypeOpt -> {
                if (expenseTypeOpt.isEmpty()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.NotFound("Expense type not found"))
                    );
                }
                return Uni.createFrom().item(Either.right(expenseTypeOpt.get()));
            });
    }

    @Override
    @WithSession
    public Uni<Either<ServiceError, List<ExpenseType>>> getAllExpenseTypes(UUID userId) {
        return expenseTypeRepository.findAllByUserId(userId)
            .chain(result -> {
                if (result.isLeft()) {
                    return Uni.createFrom().item(
                        Either.left(new ServiceError.OperationFailed(
                            "Failed to retrieve expense types: " + result.getLeft().message()
                        ))
                    );
                }
                return Uni.createFrom().item(Either.right(result.getRight()));
            });
    }
}
