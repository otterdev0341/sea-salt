package com.otterdev.repository.table;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.otterdev.entity.table.Memo;
import com.otterdev.error.repository.FetchFailedError;
import com.otterdev.error.repository.RepositoryError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class MemoRepository implements PanacheRepository<Memo> {

    // This class can be used to define custom query methods if needed.
    // For example, you can add methods like findByTitle, findByContent, etc.
    
    public Uni<Optional<Memo>> findByMemoIdAndUserId(UUID memoId, UUID userId) {
        return find("id = ?1 and createdBy.id = ?2", memoId, userId).firstResult()
                .map(Optional::ofNullable)
                .onFailure().recoverWithItem(e -> {
                    // Log the error or handle it as needed
                    return Optional.empty();
                });
    }

    public Uni<Either<RepositoryError, List<Memo>>> findAllUserByMemoTypeId(UUID memoTypeId, UUID userId) {
        return find("memoType.id = ?1 and createdBy.id = ?2", memoTypeId, userId).list()
                .map(memos -> Either.<RepositoryError, List<Memo>>right(memos))
                .onFailure().recoverWithItem(e -> Either.<RepositoryError, List<Memo>>left(new FetchFailedError("Failed to fetch memos: " + e.getMessage())));
    }

    public Uni<Optional<Memo>> findById(UUID memoId) {
        return find("memo.id = ?1", memoId).firstResult()
                .map(Optional::ofNullable)
                .onFailure().recoverWithItem(e -> {
                    // Log the error or handle it as needed
                    return Optional.empty();
                });
    }

    
}
