package com.otterdev.infrastructure.service.implService.ops;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.FileDetail;
import com.otterdev.domain.entity.Memo;
import com.otterdev.domain.valueObject.dto.file.RequestAttachFile;
import com.otterdev.domain.valueObject.dto.memo.ReqCreateMemoDto;
import com.otterdev.domain.valueObject.dto.memo.ReqUpdateMemoDto;
import com.otterdev.error_structure.ServiceError;
import com.otterdev.infrastructure.service.internal.ops.InternalMemoService;
import com.otterdev.infrastructure.service.internal.support.InternalFileRelateService;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
class MemoServiceImpl implements InternalMemoService, InternalFileRelateService {

    @Override
    public Uni<Either<ServiceError, Memo>> createMemo(ReqCreateMemoDto reqCreateMemoDto, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createMemo'");
    }

    @Override
    public Uni<Either<ServiceError, Memo>> updateMemo(UUID memoId, ReqUpdateMemoDto reqUpdateMemoDto, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateMemo'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> deleteMemo(UUID memoId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteMemo'");
    }

    @Override
    public Uni<Either<ServiceError, Memo>> getMemoById(UUID memoId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMemoById'");
    }

    @Override
    public Uni<Either<ServiceError, List<Memo>>> getAllMemos(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllMemos'");
    }

    @Override
    public Uni<Either<ServiceError, List<Memo>>> getMemosByType(UUID memoTypeId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMemosByType'");
    }

    @Override
    public Uni<Either<ServiceError, List<Memo>>> getMemosByPropertyId(UUID propertyId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMemosByPropertyId'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> addFileToMemoById(UUID memoId, RequestAttachFile file, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addFileToMemoById'");
    }

    @Override
    public Uni<Either<ServiceError, Boolean>> deleteFileFromMemoById(UUID memoId, UUID fileId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteFileFromMemoById'");
    }

    @Override
    public Uni<Either<ServiceError, List<FileDetail>>> getAllImagesRelatedById(UUID targetId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllImagesRelatedById'");
    }

    @Override
    public Uni<Either<ServiceError, List<FileDetail>>> getAllPdfRelatedById(UUID targetId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllPdfRelatedById'");
    }

    @Override
    public Uni<Either<ServiceError, List<FileDetail>>> getAllOtherFileRelatedById(UUID targetId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllOtherFileRelatedById'");
    }

    @Override
    public Uni<Either<ServiceError, List<FileDetail>>> getAllFilesRelatedById(UUID targetId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllFilesRelatedById'");
    }
    
}
