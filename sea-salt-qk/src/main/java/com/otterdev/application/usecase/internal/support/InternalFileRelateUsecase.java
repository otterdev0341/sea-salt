package com.otterdev.application.usecase.internal.support;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.FileDetail;
import com.otterdev.error_structure.UsecaseError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped  
public interface InternalFileRelateUsecase {
    
    @WithSession
    Uni<Either<UsecaseError, List<FileDetail>>> getAllImagesRelatedById(UUID targetId, UUID userId);  

    @WithSession
    Uni<Either<UsecaseError, List<FileDetail>>> getAllPdfRelatedById(UUID targetId, UUID userId);  

    @WithSession
    Uni<Either<UsecaseError, List<FileDetail>>> getAllOtherFileRelatedById(UUID targetId, UUID userId);  

    @WithSession
    Uni<Either<UsecaseError, List<FileDetail>>> getAllFilesRelatedById(UUID targetId, UUID userId);
}
