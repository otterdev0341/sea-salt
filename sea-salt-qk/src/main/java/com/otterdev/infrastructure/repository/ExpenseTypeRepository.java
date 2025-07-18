package com.otterdev.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.otterdev.domain.entity.ExpenseType;
import com.otterdev.error_structure.RepositoryError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseTypeRepository implements PanacheRepository<ExpenseType> {
    
    public Uni<Optional<ExpenseType>> findByIdAndUserId(UUID expenseTypeId, UUID userId) {
        return find("FROM ExpenseType et WHERE et.id = ?1 AND et.createdBy.id = ?2", expenseTypeId, userId)
            .firstResult()
            .map(Optional::ofNullable);
    }

    public Uni<Boolean> isExistByDetailAndUserId(String detail, UUID userId) {
        return count("detail = ?1 AND createdBy.id = ?2", detail, userId)
            .map(count -> count > 0);
    }

    public Uni<Either<RepositoryError, Boolean>> deleteExpenseType(UUID expenseTypeId, UUID userId) {
        return findByIdAndUserId(expenseTypeId, userId)
                .onItem()
                .transformToUni(expenseTypeOpt -> {
                    if (expenseTypeOpt.isEmpty()) {
                        return Uni.createFrom().item(Either.left(new RepositoryError.NotFound("Expense type not found")));
                    }
                    return delete(expenseTypeOpt.get())
                            .replaceWith(Either.right(true));
                });
    }

    public Uni<Either<RepositoryError, List<ExpenseType>>> findAllByUserId(UUID userId) {
        return find("""
                FROM ExpenseType et 
                LEFT JOIN FETCH et.createdBy u
                LEFT JOIN FETCH u.gender g
                LEFT JOIN FETCH u.role r
                WHERE et.createdBy.id = ?1
                """, userId)
            .list()
            .onItem()
            .transform(expenseTypes -> Either.<RepositoryError, List<ExpenseType>>right(expenseTypes))
            .onFailure()
            .recoverWithItem(e -> Either.<RepositoryError, List<ExpenseType>>left(
                new RepositoryError.FetchFailed("Failed to retrieve expense types: " + e.getMessage())
            ));
    }

    public Uni<Either<RepositoryError, ExpenseType>> updateExpenseType(ExpenseType expenseType, UUID userId) {
        String updateQuery = """
            UPDATE ExpenseType et 
            SET et.detail = ?1, et.updatedAt = ?2 
            WHERE et.id = ?3 AND et.createdBy.id = ?4
            """;
        
        return update(updateQuery, 
                expenseType.getDetail().trim(),
                LocalDateTime.now(),
                expenseType.getId(),
                userId)
            .chain(updatedCount -> {
                if (updatedCount == 0) {
                    return Uni.createFrom().item(
                        Either.left(new RepositoryError.NotFound("Expense type not found or not updated"))
                    );
                }
                return find("""
                        FROM ExpenseType et
                        LEFT JOIN FETCH et.createdBy u
                        LEFT JOIN FETCH u.gender g
                        LEFT JOIN FETCH u.role r
                        WHERE et.id = ?1 AND et.createdBy.id = ?2
                        """, expenseType.getId(), userId)
                    .firstResult()
                    .map(updatedExpenseType -> 
                        updatedExpenseType != null
                        ? Either.<RepositoryError, ExpenseType>right(updatedExpenseType)
                        : Either.<RepositoryError, ExpenseType>left(new RepositoryError.NotFound("Expense type not found or not updated"))
                    );
            });
    }
}
