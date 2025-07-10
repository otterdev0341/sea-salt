package com.otterdev.repository.table;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.otterdev.entity.table.Expense;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseRepository implements PanacheRepository<Expense> {

    // This class can be used to define custom query methods if needed.
    // For example, you can add methods like findByAmount, findByDate, etc.
    public Uni<Optional<Expense>> findById(UUID id) {
        return find("id", id).firstResult()
                .map(result -> Optional.ofNullable((Expense) result));
    }

    public Uni<List<Expense>> findAllByUserId(UUID userId) {
        return find("createdBy.id", userId).list();
    }
}
