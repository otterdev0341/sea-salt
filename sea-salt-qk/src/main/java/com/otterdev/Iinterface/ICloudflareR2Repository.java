package com.otterdev.Iinterface;

import java.io.InputStream;
import java.util.List;
import com.otterdev.error.repository.RepositoryError;
import com.spencerwi.either.Either;
import io.smallrye.mutiny.Uni;

public interface ICloudflareR2Repository {

    /**
     * Save a file to Cloudflare R2.
     *
     * @return Uni of Either: error or success (Void)
     */
    Uni<Either<RepositoryError, Void>> saveFile(String key, InputStream inputStream, long contentLength, String contentType);

    /**
     * Retrieve a file metadata or signed URL by key.
     *
     * @return Uni of Either: error or signed URL
     */
    Uni<Either<RepositoryError, String>> getFile(String key);

    /**
     * List all object keys under a prefix.
     *
     * @return Uni of Either: error or list of keys
     */
    Uni<Either<RepositoryError, List<String>>> getFiles(String prefix);

    /**
     * Delete a file by key or prefix.
     *
     * @return Uni of Either: error or success (Void)
     */
    Uni<Either<RepositoryError, Void>> deleteFile(String key);
}

