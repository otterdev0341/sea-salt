package com.otterdev.repository.table;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.otterdev.entity.table.ExpenseType;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class ExpenseTypeRepository implements PanacheRepository<ExpenseType> {

    // This class can be used to define custom query methods if needed.
    // For example, you can add methods like findByType, findByExpenseId, etc.
    public Uni<Boolean> existsByDetailAndCreatedBy(String detail, UUID userId) {
    return find("detail = ?1 and createdBy.id = ?2", detail, userId)
        .firstResult()
        .map(result -> result != null);
    }

    public Uni<Optional<ExpenseType>> findById(UUID id) {
        return find("id", id).firstResult()
                .map(Optional::ofNullable);
    }

    public Uni<List<ExpenseType>> findAllByUserId(UUID userId) {
        return find("createdBy.id", userId).list();
    }
}
