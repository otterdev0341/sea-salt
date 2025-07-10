package com.otterdev.repository.table;

import java.util.Optional;
import java.util.UUID;

import com.otterdev.entity.table.FileDetail;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class FileDetailRepository implements PanacheRepository<FileDetail> {

    // This class can be used to define custom query methods if needed.
    // For example, you can add methods like findByFileName, findByFileType, etc.
    public Uni<Optional<FileDetail>> fileById(UUID fileId) {
        return find("id", fileId).firstResult()
                .map(Optional::ofNullable)
                .onFailure().recoverWithItem(e -> {
                    // Log the error or handle it as needed
                    return Optional.empty();
                });
    }
}
