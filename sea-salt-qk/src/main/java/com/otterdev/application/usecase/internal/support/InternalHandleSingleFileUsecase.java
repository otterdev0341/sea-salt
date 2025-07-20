package com.otterdev.application.usecase.internal.support;

import java.util.UUID;
import com.otterdev.domain.valueObject.dto.file.RequestAttachFile;
import com.otterdev.error_structure.UsecaseError;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface InternalHandleSingleFileUsecase {

    public Uni<Either<UsecaseError, Boolean>> attachFileToTarget(UUID targetId, RequestAttachFile requestAttachFile, UUID userId);

    public Uni<Either<UsecaseError, Boolean>> removeFileFromTarget(UUID taretId, UUID fileId, UUID userId);
}
