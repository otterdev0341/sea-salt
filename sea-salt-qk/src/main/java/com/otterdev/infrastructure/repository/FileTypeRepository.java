package com.otterdev.infrastructure.repository;

import java.util.Optional;

import com.otterdev.domain.entity.FileType;
import com.otterdev.error_structure.RepositoryError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileTypeRepository implements PanacheRepository<FileType> {
    
    // Add methods to interact with FileType entities as needed
    // For example, you might want to find a FileType by its name or ID

    public Uni<Optional<FileType>> findByDetail(String detail) {
        return find("detail", detail).firstResult()
            .map(Optional::ofNullable);
    }

    public Uni<Either<RepositoryError, FileType>> getFileTypeByExtention(String extention) {
        String ext = extention.toLowerCase().trim();
        
        // Image file types
        if (ext.matches("^(jpg|jpeg|png|gif|svg|webp|image)$")) {
            return getImageFileType()
            .chain(result -> result.fold(
                error -> Uni.createFrom().item(Either.left(error)),
                fileType -> Uni.createFrom().item(Either.right(fileType))
            ));
        }
        
        // PDF files
        if (ext.equals("pdf")) {
            return getPdfFileType()
            .chain(result -> result.fold(
                error -> Uni.createFrom().item(Either.left(error)),
                fileType -> Uni.createFrom().item(Either.right(fileType))
            ));
        }
        
        // All other file types
        return getImageOtherType()
        .chain(result -> result.fold(
                error -> Uni.createFrom().item(Either.left(error)),
                fileType -> Uni.createFrom().item(Either.right(fileType))
            ));
    }

    // -- internal funtions to get specific file types
    // These methods can be modified to suit your application's needs

    
    private Uni<Either<RepositoryError, FileType>> getImageFileType() {
        // return > 0 returns the first result or fails if not found
        return find("detail", "image").firstResult()
            .onItem().transform(fileType -> Either.<RepositoryError, FileType>right(fileType))
            .onFailure().recoverWithItem(err ->
                Either.<RepositoryError, FileType>left(new RepositoryError.FetchFailed("Failed to fetch image file type: " + err.getMessage()))
            );
    }

    private Uni<Either<RepositoryError,FileType>> getPdfFileType() {
        return find("detail", "pdf").firstResult()
            .onItem().transform(fileType -> Either.<RepositoryError, FileType>right(fileType))
            .onFailure().recoverWithItem(err ->
                Either.<RepositoryError, FileType>left(new RepositoryError.FetchFailed("Failed to fetch pdf file type: " + err.getMessage()))
            );
    }

    private Uni<Either<RepositoryError, FileType>> getImageOtherType() {
        return find("detail", "other").firstResult()
            .onItem().transform(fileType -> Either.<RepositoryError, FileType>right(fileType))
            .onFailure().recoverWithItem(err ->
                Either.<RepositoryError, FileType>left(new RepositoryError.FetchFailed("Failed to fetch other file type: " + err.getMessage()))
            );
    }

    
}
