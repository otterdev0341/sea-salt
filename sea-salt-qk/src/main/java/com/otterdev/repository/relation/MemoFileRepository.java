package com.otterdev.repository.relation;

import java.util.Optional;
import java.util.UUID;

import com.otterdev.entity.relation.MemoFile;
import com.otterdev.error.repository.FetchFailedError;
import com.otterdev.error.repository.NotFoundError;
import com.otterdev.error.repository.RepositoryError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

public class MemoFileRepository implements PanacheRepository<MemoFile> {

    // This class can be used to define custom query methods if needed.
    // For example, you can add methods like findByMemoId, findByFileId, etc.

    // Implement custom methods here as needed.
    
    public Uni<Either<RepositoryError, Optional<MemoFile>>> findByMemoIdAndUserId(UUID memoId, UUID userId) {
        return find("memo.id = ?1 and user.id = ?2", memoId, userId).firstResult()
                .onItem().<Either<RepositoryError, Optional<MemoFile>>>transform(memoFile -> {
                    if (memoFile == null) {
                        return Either.<RepositoryError, Optional<MemoFile>>left(
                            new NotFoundError("MemoFile not found for memoId: " + memoId + " and userId: " + userId)
                        );
                    }
                    return Either.<RepositoryError, Optional<MemoFile>>right(Optional.of(memoFile));
                })
                .onFailure().recoverWithItem(throwable -> 
                    Either.<RepositoryError, Optional<MemoFile>>left(
                        new FetchFailedError("Failed to fetch MemoFile by memoId and userId: " + throwable.getMessage())
                    )
                );
    }
}
