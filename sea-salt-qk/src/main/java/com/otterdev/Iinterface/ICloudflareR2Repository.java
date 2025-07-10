package com.otterdev.Iinterface;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import com.otterdev.dto.cloudflare.ResFileR2Dto;
import com.otterdev.error.repository.RepositoryError;
import com.spencerwi.either.Either;
import io.smallrye.mutiny.Uni;

public interface ICloudflareR2Repository {

    /**
     * Save a file to Cloudflare R2.
     *
     * @return Uni of Either: error or success (Void)
     */
    Uni<Either<RepositoryError, ResFileR2Dto>> saveFile(String key, InputStream inputStream, long contentLength, String contentType);

    /**
     * Retrieve a file metadata or signed URL by key.
     *
     * @return Uni of Either: error or signed URL
     */
    Uni<Either<RepositoryError, Optional<ResFileR2Dto>>> getFile(String key);

    /**
     * List all object keys under a prefix.
     *
     * @return Uni of Either: error or list of keys
     */
    Uni<Either<RepositoryError, List<ResFileR2Dto>>> getFiles(String prefix);

    /**
     * Delete a file by key or prefix.
     *
     * @return Uni of Either: error or success (Void)
     */
    Uni<Either<RepositoryError, Void>> deleteFile(String key);
}

