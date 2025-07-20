package com.otterdev.infrastructure.repository;

import java.util.List;
import java.util.UUID;

import org.hibernate.reactive.mutiny.Mutiny;

import com.otterdev.domain.entity.FileDetail;
import com.otterdev.domain.entity.FileType;
import com.otterdev.domain.entity.relation.PropertyFileDetail;
import com.otterdev.error_structure.RepositoryError;
import com.otterdev.infrastructure.repository.support.InternalFileRelateRepository;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
@Named("propertyFileDetailRepository")
public class PropertyFileDetailRepository implements PanacheRepository<PropertyFileDetail>, InternalFileRelateRepository {

    @Inject
    Mutiny.SessionFactory sessionFactory;

    @Override
    public Uni<Either<RepositoryError, List<FileDetail>>> getAllFileRelatedById(UUID targetId, UUID userId, FileType fileType) {
        String query = """
            SELECT fd
            FROM PropertyFileDetail pfd
            JOIN pfd.file fd
            LEFT JOIN FETCH fd.type t
            LEFT JOIN FETCH fd.createdBy u
            WHERE pfd.property.id = :targetId
              AND pfd.createdBy.id = :userId
              AND fd.type.id = :fileTypeId
        """;

        return sessionFactory.withSession(session ->
            session.createQuery(query, FileDetail.class)
                .setParameter("targetId", targetId)
                .setParameter("userId", userId)
                .setParameter("fileTypeId", fileType.getId())
                .getResultList()
        )
        .map(fileDetails -> {
            if (fileDetails == null || fileDetails.isEmpty()) {
                return Either.<RepositoryError, List<FileDetail>>left(
                    new RepositoryError.NotFound("No image files found for the given property and user")
                );
            }
            return Either.<RepositoryError, List<FileDetail>>right(fileDetails);
        })
        .onFailure()
        .recoverWithItem(err ->
            Either.<RepositoryError, List<FileDetail>>left(
                new RepositoryError.FetchFailed("Failed to fetch image files: " + err.getMessage())
            )
        );
    }


    @Override
    public Uni<Either<RepositoryError, List<FileDetail>>> getAllFilesRelatedById(UUID targetId, UUID userId) {
            String query = """
            SELECT fd
            FROM PropertyFileDetail pfd
            JOIN pfd.file fd
            LEFT JOIN FETCH fd.type t
            LEFT JOIN FETCH fd.createdBy u
            WHERE pfd.property.id = :targetId
            AND pfd.createdBy.id = :userId
        """;

        return sessionFactory.withSession(session ->
            session.createQuery(query, FileDetail.class)
                .setParameter("targetId", targetId)
                .setParameter("userId", userId)
                .getResultList()
        )
        .map(fileDetails -> {
            if (fileDetails == null || fileDetails.isEmpty()) {
                return Either.<RepositoryError, List<FileDetail>>left(
                    new RepositoryError.NotFound("No files found for the given property and user")
                );
            }
            return Either.<RepositoryError, List<FileDetail>>right(fileDetails);
        })
        .onFailure()
        .recoverWithItem(err ->
            Either.<RepositoryError, List<FileDetail>>left(
                new RepositoryError.FetchFailed("Failed to fetch files: " + err.getMessage())
            )
        );
    }
    
    // This repository can be used to manage FileDetail entities related to properties.
    // Additional methods specific to PropertyFileDetail can be added here if needed.
    
}
