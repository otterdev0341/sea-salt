package com.otterdev.application.usecase.implUsecase.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.application.usecase.internal.base.InternalMemoTypeUsecase;
import com.otterdev.domain.entity.MemoType;
import com.otterdev.domain.valueObject.dto.memoType.ReqCreateMemoTypeDto;
import com.otterdev.domain.valueObject.dto.memoType.ReqUpdateMemoTypeDto;
import com.otterdev.error_structure.UsecaseError;
import com.otterdev.infrastructure.service.internal.base.InternalMemoTypeService;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
class MemoTypeUsecaseImpl implements InternalMemoTypeUsecase {

    private final InternalMemoTypeService memoTypeService;

    @Inject
    public MemoTypeUsecaseImpl(InternalMemoTypeService memoTypeService) {
        this.memoTypeService = memoTypeService;
    }


    @Override
    @WithTransaction
    public Uni<Either<UsecaseError, MemoType>> createMemoType(ReqCreateMemoTypeDto reqCreateMemoTypeDto, UUID userId) {
        
        return memoTypeService.createMemoType(reqCreateMemoTypeDto, userId)
               .chain(result -> {
                   if (result.isLeft()) {
                       return Uni.createFrom().item(Either.left(new UsecaseError.BusinessError( "Failed to create memo typ cause by : " + result.getLeft().message())));
                   } else {
                       return Uni.createFrom().item(Either.right(result.getRight()));
                   }
               });
    }

    @Override
    @WithTransaction
    public Uni<Either<UsecaseError, MemoType>> updateMemoType(UUID memoTypeId,
            ReqUpdateMemoTypeDto reqUpdateMemoTypeDto, UUID userId) {

        return memoTypeService.updateMemoType(memoTypeId, reqUpdateMemoTypeDto, userId)
               .chain(result -> {
                   if (result.isLeft()) {
                       return Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to update memo type cause by : " + result.getLeft().message())));
                   } else {
                       return Uni.createFrom().item(Either.right(result.getRight()));
                   }
               });
    }

    @Override
    @WithTransaction
    public Uni<Either<UsecaseError, Boolean>> deleteMemoType(UUID memoTypeId, UUID userId) {
        
        return memoTypeService.deleteMemoType(memoTypeId, userId)
               .chain(result -> {
                   if (result.isLeft()) {
                       return Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to delete memo type cause by : " + result.getLeft().message())));
                   } else {
                       return Uni.createFrom().item(Either.right(result.getRight()));
                   }
               });
    }

    @Override
    @WithSession
    public Uni<Either<UsecaseError, MemoType>> getMemoTypeById(UUID memoTypeId, UUID userId) {
        
        return memoTypeService.getMemoType(memoTypeId, userId)
                .chain(result -> {
                     if (result.isLeft()) {
                          return Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to get memo type cause by : " + result.getLeft().message())));
                     } else {
                          return Uni.createFrom().item(Either.right(result.getRight()));
                     }
                });
    }

    @Override
    @WithSession
    public Uni<Either<UsecaseError, List<MemoType>>> getAllMemoTypes(UUID userId) {
        
        return memoTypeService.getAllMemoTypes(userId)
                .chain(result -> {
                    if (result.isLeft()) {
                        return Uni.createFrom().item(Either.left(new UsecaseError.BusinessError("Failed to get all memo types cause by : " + result.getLeft().message())));
                    } else {
                        return Uni.createFrom().item(Either.right(result.getRight()));
                    }
                });
    }
    
}
