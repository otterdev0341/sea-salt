package com.otterdev.infrastructure.repository.support;

import java.util.List;
import java.util.UUID;

import com.otterdev.domain.entity.FileDetail;
import com.otterdev.domain.entity.FileType;
import com.otterdev.error_structure.RepositoryError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped  
public interface InternalFileRelateRepository {
    
    @WithSession
    Uni<Either<RepositoryError, List<FileDetail>>> getAllFileRelatedById(UUID targetId, UUID userId, FileType fileType);  


    @WithSession
    Uni<Either<RepositoryError, List<FileDetail>>> getAllFilesRelatedById(UUID targetId, UUID userId);
}
