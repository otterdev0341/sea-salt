package com.otterdev.repository.table;

import java.util.Optional;
import java.util.UUID;

import com.otterdev.entity.table.MemoType;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class MemoTypeRepository implements PanacheRepository<MemoType> {

    // This class can be used to define custom query methods if needed.
    // For example, you can add methods like findByType, findByMemoId, etc.
    public Uni<Boolean> existsByDetail(String title) {
        return find("detail", title).count()
                .map(count -> count > 0);
    }
    
    public Uni<Optional<MemoType>> findByIdAndUserId(UUID memoTypeId, UUID userId) {
        return find("id = ?1 and createdBy.id = ?2", memoTypeId, userId).firstResult()
                .map(Optional::ofNullable);
    }
}
