package com.otterdev.domain.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.otterdev.domain.entity.relation.InvestmentTransactionFileDetail;
import com.otterdev.domain.entity.relation.MemoFileDetail;
import com.otterdev.domain.entity.relation.PaymentTransactionFileDetail;
import com.otterdev.domain.entity.relation.PropertyFileDetail;
import com.otterdev.domain.entity.relation.SaleTransactionFileDetail;

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
    name = "file_details",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_file_details_name_created_by",
            columnNames = {"name", "created_by", "type"}
        )
    },
    indexes = {
        @Index(
            name = "idx_file_details_created_by",
            columnList = "created_by"
        )
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "object_key", nullable = false, length = 255)
    private String objectKey;

    @Column(name = "path", nullable = false, length = 255)
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "type",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_file_details_type",
            foreignKeyDefinition = 
                "FOREIGN KEY (type) REFERENCES file_types(id) " +
                "ON DELETE RESTRICT ON UPDATE CASCADE"
        )
    )
    private FileType type;

    @Column(name = "size", nullable = false)
    private Long size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "created_by",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(
            name = "fk_file_details_created_by",
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
    
    @OneToMany(mappedBy = "file")
    private Set<PropertyFileDetail> properties = new HashSet<>();

    @OneToMany(mappedBy = "file")
    private Set<MemoFileDetail> memos = new HashSet<>();

    @OneToMany(mappedBy = "file")
    private Set<SaleTransactionFileDetail> saleTransactions = new HashSet<>();

    @OneToMany(mappedBy = "file")
    private Set<InvestmentTransactionFileDetail> investTransactions = new HashSet<>();

    @OneToMany(mappedBy = "file")
    private Set<PaymentTransactionFileDetail> paymentTransactions = new HashSet<>();

}
