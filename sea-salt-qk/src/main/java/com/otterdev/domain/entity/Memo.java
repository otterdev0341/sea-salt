package com.otterdev.domain.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.otterdev.domain.entity.relation.MemoFileDetail;
import com.otterdev.domain.entity.relation.MemoProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "memos",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_memos_name_created_by",
            columnNames = {"name", "created_by"}
        )
    },
    indexes = {
        @Index(
            name = "idx_memos_created_by",
            columnList = "created_by"
        ),
        @Index(
            name = "idx_memos_memo_type",
            columnList = "memo_type"
        )
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Memo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "detail", length = 255)
    private String detail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "memo_type",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_memos_memo_type",
            foreignKeyDefinition = 
                "FOREIGN KEY (memo_type) REFERENCES memo_types(id) " +
                "ON DELETE RESTRICT ON UPDATE CASCADE"
        )
    )
    private MemoType memoType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "created_by",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(
            name = "fk_memos_created_by",
            foreignKeyDefinition = 
                "FOREIGN KEY (created_by) REFERENCES users(id) " +
                "ON DELETE RESTRICT ON UPDATE CASCADE"
        )
    )
    private User createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // relation
    @OneToMany(mappedBy = "memo")
    private Set<MemoFileDetail> fileDetails = new HashSet<>();

    @OneToMany(mappedBy = "memo")
    private Set<MemoProperty> properties = new HashSet<>();
}
