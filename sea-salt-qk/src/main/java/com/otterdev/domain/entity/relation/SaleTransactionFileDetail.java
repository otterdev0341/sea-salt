package com.otterdev.domain.entity.relation;

import java.time.LocalDateTime;
import java.util.UUID;

import com.otterdev.domain.entity.FileDetail;
import com.otterdev.domain.entity.SaleTransaction;
import com.otterdev.domain.entity.User;

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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "sale_transaction_file_details",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_sale_transaction_file_details_relation",
            columnNames = {"sale_transaction", "file", "created_by"}
        )
    },
    indexes = {
        @Index(
            name = "idx_sale_transaction_file_details_created_by",
            columnList = "created_by"
        )
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleTransactionFileDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "sale_transaction",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_sale_transaction_file_details_sale",
            foreignKeyDefinition = 
                "FOREIGN KEY (sale_transaction) REFERENCES sale_transactions(id) " +
                "ON DELETE RESTRICT ON UPDATE CASCADE"
        )
    )
    private SaleTransaction saleTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "file",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_sale_transaction_file_details_file",
            foreignKeyDefinition = 
                "FOREIGN KEY (file) REFERENCES file_details(id) " +
                "ON DELETE RESTRICT ON UPDATE CASCADE"
        )
    )
    private FileDetail file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "created_by",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(
            name = "fk_sale_transaction_file_details_created_by",
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
}
