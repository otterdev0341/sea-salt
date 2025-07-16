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
    public Uni<Either<UsecaseError, MemoType>> createMemoType(ReqCreateMemoTypeDto reqCreateMemoTypeDto, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createMemoType'");
    }

    @Override
    public Uni<Either<UsecaseError, MemoType>> updateMemoType(UUID memoTypeId,
            ReqUpdateMemoTypeDto reqUpdateMemoTypeDto, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateMemoType'");
    }

    @Override
    public Uni<Either<UsecaseError, Boolean>> deleteMemoType(UUID memoTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteMemoType'");
    }

    @Override
    public Uni<Either<UsecaseError, MemoType>> getMemoTypeById(UUID memoTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMemoType'");
    }

    @Override
    public Uni<Either<UsecaseError, List<MemoType>>> getAllMemoTypes(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllMemoTypes'");
    }
    
}
