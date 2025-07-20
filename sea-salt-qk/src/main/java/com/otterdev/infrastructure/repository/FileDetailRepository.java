package com.otterdev.infrastructure.repository;

import com.otterdev.domain.entity.FileDetail;
import com.otterdev.error_structure.RepositoryError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileDetailRepository implements PanacheRepository<FileDetail> {
    
    public Uni<Either<RepositoryError, FileDetail>> getImageType() {
        return find("FROM FileDetail fd WHERE fd.name = ?1", "image")
            .firstResult()
            .onItem()
            .transform(fileDetail -> Either.<RepositoryError, FileDetail>right(fileDetail))
            .onFailure()
            .recoverWithItem(e -> Either.<RepositoryError, FileDetail>left(
                new RepositoryError.FetchFailed("Failed to retrieve image type: " + e.getMessage())
            ));
    }

    public Uni<Either<RepositoryError, FileDetail>> getPdfType() {
        return find("FROM FileDetail fd WHERE fd.name = ?1", "pdf")
            .firstResult()
            .onItem()
            .transform(fileDetail -> Either.<RepositoryError, FileDetail>right(fileDetail))
            .onFailure()
            .recoverWithItem(e -> Either.<RepositoryError, FileDetail>left(
                new RepositoryError.FetchFailed("Failed to retrieve pdf type: " + e.getMessage())
            ));
    }

    public Uni<Either<RepositoryError, FileDetail>> getOtherType() {
        return find("FROM FileDetail fd WHERE fd.name = ?1", "other")
            .firstResult()
            .onItem()
            .transform(fileDetail -> Either.<RepositoryError, FileDetail>right(fileDetail))
            .onFailure()
            .recoverWithItem(e -> Either.<RepositoryError, FileDetail>left(
                new RepositoryError.FetchFailed("Failed to retrieve other type: " + e.getMessage())
            ));
    }

}
