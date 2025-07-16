package com.otterdev.infrastructure.service.internal.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.MemoType;
import com.otterdev.domain.valueObject.dto.memoType.ReqCreateMemoTypeDto;
import com.otterdev.domain.valueObject.dto.memoType.ReqUpdateMemoTypeDto;
import com.otterdev.error_structure.ServiceError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalMemoTypeService {
    
    @WithTransaction
    Uni<Either<ServiceError, MemoType>> createMemoType(ReqCreateMemoTypeDto reqCreateMemoTypeDto, UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, MemoType>> updateMemoType(UUID memoTypeId, ReqUpdateMemoTypeDto reqUpdateMemoTypeDto, UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, Boolean>> deleteMemoType(UUID memoTypeId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, MemoType>> getMemoType(UUID memoTypeId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, List<MemoType>>> getAllMemoTypes(UUID userId);

}
