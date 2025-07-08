package com.otterdev.entity.table;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "memo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "memo_type", "created_by"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name= "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "memo_type", referencedColumnName = "id", nullable = false)
    private MemoType memoType;

    @Column(name= "detail")
    private String detail;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false)
    private User createdBy;

    @Column(name= "created_at")
    private LocalDateTime createdAt;
    
    @Column(name= "updated_at")
    private LocalDateTime updatedAt;
}
