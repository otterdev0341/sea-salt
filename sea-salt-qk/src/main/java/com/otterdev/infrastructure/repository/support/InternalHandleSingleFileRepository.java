package com.otterdev.infrastructure.repository.support;

import java.util.UUID;

import com.otterdev.domain.entity.FileDetail;
import com.otterdev.error_structure.RepositoryError;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalHandleSingleFileRepository {
    
    public Uni<Either<RepositoryError, Boolean>> attachFileToTarget(UUID targetId, FileDetail fileDetail);

    public Uni<Either<RepositoryError, Boolean>> removeFileFromTarget(UUID taretId, UUID fileId);

}
