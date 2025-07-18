package com.otterdev.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.otterdev.domain.entity.MemoType;
import com.otterdev.error_structure.RepositoryError;
import com.spencerwi.either.Either;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MemoTypeRepository implements PanacheRepository<MemoType> {
    
    public Uni<Optional<MemoType>> findByIdAndUserId(UUID memoTypeId, UUID userId) {
        // Changed query to use created_by_id instead of comparing with User entity
        return find("FROM MemoType mt WHERE mt.id = ?1 AND mt.createdBy.id = ?2", memoTypeId, userId)
            .firstResult()
            .map(Optional::ofNullable);
    }

    public Uni<Boolean> isExistByDetailAndUserId(String detail, UUID userId) {
        // Fixed query to use count and proper parameter binding
        return count("detail = ?1 AND createdBy.id = ?2", detail, userId)
            .map(count -> count > 0);
    }

    public Uni<Either<RepositoryError, Boolean>> deleteMemoType(UUID memoTypeId, UUID userId) {
        return findByIdAndUserId(memoTypeId, userId)
                .onItem()
                .transformToUni(memoTypeOpt -> {
                    if (memoTypeOpt.isEmpty()) {
                        return Uni.createFrom().item(Either.left(new RepositoryError.NotFound("Memo type not found")));
                    }
                    return delete(memoTypeOpt.get())
                            .replaceWith(Either.right(true));
                });
    }

    public Uni<Either<RepositoryError, List<MemoType>>> findAllByUserId(UUID userId) {
        return find("""
            FROM MemoType mt 
            LEFT JOIN FETCH mt.createdBy u 
            LEFT JOIN FETCH u.gender g 
            LEFT JOIN FETCH u.role r 
            WHERE mt.createdBy.id = ?1
            """, userId)
            .list()
            .onItem()
            .transform(memoTypes -> Either.<RepositoryError, List<MemoType>>right(memoTypes))
            .onFailure()
            .recoverWithItem(e -> Either.<RepositoryError, List<MemoType>>left(
                new RepositoryError.FetchFailed("Failed to retrieve memo types: " + e.getMessage())
            ));
    }

    public Uni<Either<RepositoryError, MemoType>> updateMemoType(MemoType memoType, UUID userId) {
        String updateQuery = """
            UPDATE FROM MemoType mt 
            SET mt.detail = ?1, mt.updatedAt = ?2 
            WHERE mt.id = ?3 AND mt.createdBy.id = ?4
            """;
            
        return update(updateQuery, 
                memoType.getDetail().trim(),
                LocalDateTime.now(),
                memoType.getId(),
                userId)
            .chain(updatedCount -> {
                if (updatedCount == 0) {
                    return Uni.createFrom().item(
                        Either.left(new RepositoryError.NotFound("Memo type not found or unauthorized"))
                    );
                }
                
                // Fetch updated entity with all relations
                return find("""
                    FROM MemoType mt 
                    LEFT JOIN FETCH mt.createdBy u 
                    LEFT JOIN FETCH u.gender g 
                    LEFT JOIN FETCH u.role r 
                    WHERE mt.id = ?1 AND mt.createdBy.id = ?2
                    """, memoType.getId(), userId)
                    .firstResult()
                    .map(updatedMemoType -> 
                        updatedMemoType != null 
                            ? Either.<RepositoryError, MemoType>right(updatedMemoType)
                            : Either.<RepositoryError, MemoType>left(
                                new RepositoryError.NotFound("Failed to fetch updated memo type")
                            )
                    );
            });
    }

}
