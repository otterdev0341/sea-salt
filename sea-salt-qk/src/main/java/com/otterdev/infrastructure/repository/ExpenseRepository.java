package com.otterdev.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.otterdev.domain.entity.Expense;
import com.otterdev.domain.entity.ExpenseType;
import com.otterdev.error_structure.RepositoryError;
import com.spencerwi.either.Either;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseRepository implements PanacheRepository<Expense> {
    
    public Uni<Optional<Expense>> findByIdAndUserId(UUID expenseId, UUID userId) {
        return find("FROM Expense e WHERE e.id = ?1 AND e.createdBy.id = ?2", expenseId, userId)
            .firstResult()
            .map(Optional::ofNullable);
    }

    public Uni<Either<RepositoryError, Boolean>> deleteExpense(UUID expenseId, UUID userId) {
        return findByIdAndUserId(expenseId, userId)
                .onItem()
                .transformToUni(expenseOpt -> {
                    if (expenseOpt.isEmpty()) {
                        return Uni.createFrom().item(Either.left(new RepositoryError.NotFound("Expense not found")));
                    }
                    return delete(expenseOpt.get())
                            .replaceWith(Either.right(true));
                });
    }

    public Uni<Either<RepositoryError, List<Expense>>> findAllByUserId(UUID userId) {
        return find("""
                FROM Expense e 
                LEFT JOIN FETCH e.createdBy u
                LEFT JOIN FETCH u.gender g
                LEFT JOIN FETCH u.role r
                WHERE e.createdBy.id = ?1
                """, userId)
            .list()
            .onItem()
            .transform(expenses -> Either.<RepositoryError, List<Expense>>right(expenses))
            .onFailure()
            .recoverWithItem(e -> Either.<RepositoryError, List<Expense>>left(
                new RepositoryError.FetchFailed("Failed to retrieve expenses: " + e.getMessage())
            ));
    }

    public Uni<Either<RepositoryError, Expense>> updateExpense(Expense expense, UUID userId) {
        String updateQuery = """
            UPDATE Expense e
            SET e.detail = ?1, e.expenseType = ?2, e.updatedAt = ?3
            WHERE e.id = ?4 AND e.createdBy.id = ?5
            """;

        return update(updateQuery,
                expense.getDetail().trim(),
                expense.getExpenseType(),
                LocalDateTime.now(),
                expense.getId(),
                userId)
            .chain(updatedCount -> {
                if (updatedCount == 0) {
                    return Uni.createFrom().item(
                        Either.left(new RepositoryError.NotFound("Expense not found or not updated"))
                    );
                }
                return find("""
                        FROM Expense e
                        LEFT JOIN FETCH e.expenseType et
                        LEFT JOIN FETCH e.createdBy u
                        LEFT JOIN FETCH u.gender g
                        LEFT JOIN FETCH u.role r
                        WHERE e.id = ?1 AND u.id = ?2
                        """, expense.getId(), userId)
                    .firstResult()
                    .map(updatedExpense ->
                        updatedExpense != null
                            ? Either.<RepositoryError, Expense>right(updatedExpense)
                            : Either.<RepositoryError, Expense>left(new RepositoryError.NotFound("Expense not found or not updated"))
                    );
            });
    }

    public Uni<Either<RepositoryError, List<Expense>>> findByExpenseTypeAndUserId(ExpenseType expenseType, UUID userId) {
        String query = """
            FROM Expense e
            LEFT JOIN FETCH e.expenseType et
            LEFT JOIN FETCH e.createdBy u
            LEFT JOIN FETCH u.gender g
            LEFT JOIN FETCH u.role r
            WHERE et.id = ?1 AND u.id = ?2
            """;

        return find(query, expenseType.getId(), userId)
            .list()
            .map(expenses -> {
                if (expenses == null || expenses.isEmpty()) {
                    return Either.left(new RepositoryError.NotFound(
                        "No expenses found for the given expense type and user."
                    ));
                }
                return Either.<RepositoryError, List<Expense>>right(expenses);
            });
    }


}
