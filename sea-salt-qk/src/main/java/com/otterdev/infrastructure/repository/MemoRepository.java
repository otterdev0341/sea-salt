package com.otterdev.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;
import com.otterdev.domain.entity.Memo;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MemoRepository implements PanacheRepository<Memo> {
    
    public Uni<Optional<Memo>> findByIdAndUserId(UUID memoId, UUID userId) {
        return find("id = ?1 and createdBy = ?2", memoId, userId)
                .firstResult()
                .onItem()
                .transform(Optional::ofNullable);
    }

}
