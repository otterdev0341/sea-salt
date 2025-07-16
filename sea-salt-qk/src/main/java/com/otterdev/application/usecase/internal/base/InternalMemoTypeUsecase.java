package com.otterdev.application.usecase.internal.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.MemoType;
import com.otterdev.domain.valueObject.dto.memoType.ReqCreateMemoTypeDto;
import com.otterdev.domain.valueObject.dto.memoType.ReqUpdateMemoTypeDto;
import com.otterdev.error_structure.UsecaseError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalMemoTypeUsecase {
    
    @WithTransaction
    Uni<Either<UsecaseError, MemoType>> createMemoType(ReqCreateMemoTypeDto reqCreateMemoTypeDto, UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, MemoType>> updateMemoType(UUID memoTypeId, ReqUpdateMemoTypeDto reqUpdateMemoTypeDto, UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> deleteMemoType(UUID memoTypeId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, MemoType>> getMemoTypeById(UUID memoTypeId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, List<MemoType>>> getAllMemoTypes(UUID userId);

}
