package com.otterdev.application.usecase.internal.ops;

import java.util.List;
import java.util.UUID;
import com.otterdev.domain.entity.Memo;
import com.otterdev.domain.valueObject.dto.file.RequestAttachFile;
import com.otterdev.domain.valueObject.dto.memo.ReqCreateMemoDto;
import com.otterdev.domain.valueObject.dto.memo.ReqUpdateMemoDto;
import com.otterdev.error_structure.UsecaseError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalMemoUsecase {
    
    // common case
    @WithTransaction
    Uni<Either<UsecaseError, Memo>> createMemo(ReqCreateMemoDto reqCreateMemoDto, UUID userId);
    
    @WithTransaction
    Uni<Either<UsecaseError, Memo>> updateMemo(UUID memoId, ReqUpdateMemoDto reqUpdateMemoDto, UUID userId);

    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> deleteMemo(UUID memoId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, Memo>> getMemoById(UUID memoId, UUID userId);

    @WithSession
    Uni<Either<UsecaseError, List<Memo>>> getAllMemos(UUID userId);

    // query/filter case
    @WithSession
    Uni<Either<UsecaseError, List<Memo>>> getMemosByType(UUID memoTypeId, UUID userId);

    // relation case
    // -- need implement filedetail interface

    // -- property section
    @WithSession
    Uni<Either<UsecaseError, List<Memo>>> getMemosByPropertyId(UUID propertyId, UUID userId);

    // specific case
    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> addFileToMemoById(UUID memoId, RequestAttachFile file, UUID userId);
    
    @WithTransaction
    Uni<Either<UsecaseError, Boolean>> deleteFileFromMemoById(UUID memoId, UUID fileId, UUID userId);
}
