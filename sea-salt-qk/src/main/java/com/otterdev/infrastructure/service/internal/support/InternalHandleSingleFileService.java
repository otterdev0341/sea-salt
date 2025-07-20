package com.otterdev.infrastructure.service.internal.support;

import java.util.UUID;
import com.otterdev.domain.valueObject.dto.file.RequestAttachFile;
import com.otterdev.error_structure.ServiceError;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalHandleSingleFileService {

    public Uni<Either<ServiceError, Boolean>> attachFileToTarget(UUID targetId, RequestAttachFile requestAttachFile, UUID userId);

    public Uni<Either<ServiceError, Boolean>> removeFileFromTarget(UUID taretId, UUID fileId, UUID userId);
}
