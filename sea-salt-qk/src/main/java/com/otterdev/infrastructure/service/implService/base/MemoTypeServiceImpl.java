package com.otterdev.infrastructure.service.implService.base;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.MemoType;
import com.otterdev.domain.valueObject.dto.memoType.ReqCreateMemoTypeDto;
import com.otterdev.domain.valueObject.dto.memoType.ReqUpdateMemoTypeDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.repository.MemoTypeRepository;
import com.otterdev.infrastructure.service.internal.base.InternalMemoTypeService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import io.vertx.core.cli.annotations.Name;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
@Named("memoTypeService")  // Add this qualifier
class MemoTypeServiceImpl implements InternalMemoTypeService {

    private final MemoTypeRepository memoTypeRepository;

    @Inject
    public MemoTypeServiceImpl(MemoTypeRepository memoTypeRepository) {
        this.memoTypeRepository = memoTypeRepository;
    }

    @Override
    public Uni<Either<ServiceError, MemoType>> createMemoType(ReqCreateMemoTypeDto reqCreateMemoTypeDto, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createMemoType'");
    }

    @Override
    public Uni<Either<ServiceError, MemoType>> updateMemoType(UUID memoTypeId,
            ReqUpdateMemoTypeDto reqUpdateMemoTypeDto, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateMemoType'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> deleteMemoType(UUID memoTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteMemoType'");
    }

    @Override
    public Uni<Either<ServiceError, MemoType>> getMemoType(UUID memoTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMemoType'");
    }

    @Override
    public Uni<Either<ServiceError, List<MemoType>>> getAllMemoTypes(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllMemoTypes'");
    }
    
}
