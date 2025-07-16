package com.otterdev.infrastructure.service.internal.ops;

import java.util.List;
import java.util.UUID;
import com.otterdev.domain.entity.Memo;
import com.otterdev.domain.valueObject.dto.file.RequestAttachFile;
import com.otterdev.domain.valueObject.dto.memo.ReqCreateMemoDto;
import com.otterdev.domain.valueObject.dto.memo.ReqUpdateMemoDto;
import com.otterdev.error_structure.ServiceError;
import com.spencerwi.either.Either;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalMemoService {
    
    // common case
    @WithTransaction
    Uni<Either<ServiceError, Memo>> createMemo(ReqCreateMemoDto reqCreateMemoDto, UUID userId);
    
    @WithTransaction
    Uni<Either<ServiceError, Memo>> updateMemo(UUID memoId, ReqUpdateMemoDto reqUpdateMemoDto, UUID userId);

    @WithTransaction
    Uni<Either<ServiceError, Boolean>> deleteMemo(UUID memoId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, Memo>> getMemoById(UUID memoId, UUID userId);

    @WithSession
    Uni<Either<ServiceError, List<Memo>>> getAllMemos(UUID userId);

    // query/filter case
    @WithSession
    Uni<Either<ServiceError, List<Memo>>> getMemosByType(UUID memoTypeId, UUID userId);

    // relation case
    // -- need implement filedetail interface

    // -- property section
    @WithSession
    Uni<Either<ServiceError, List<Memo>>> getMemosByPropertyId(UUID propertyId, UUID userId);

    // specific case
    @WithTransaction
    Uni<Either<ServiceError, Boolean>> addFileToMemoById(UUID memoId, RequestAttachFile file, UUID userId);
    
    @WithTransaction
    Uni<Either<ServiceError, Boolean>> deleteFileFromMemoById(UUID memoId, UUID fileId, UUID userId);
}
