package com.otterdev.infrastructure.repository.internal;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.otterdev.domain.valueObject.cloudflare.ResFileR2Dto;
import com.otterdev.error_structure.RepositoryError;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public interface CloudFlareR2Repository {
    // Define methods for interacting with CloudFlare R2
    // For example:
    Uni<Either<RepositoryError, ResFileR2Dto>> uploadFile(FileUpload file);
    
    Uni<Either<RepositoryError, ResFileR2Dto>> getFile(String objectKey);
    
    Uni<Either<RepositoryError, Boolean>> deleteFile(String bucketName, String filePath);
    
    
}
