package com.otterdev.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    name = "sale_transactions",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_sale_transactions_property_transaction_created_by",
            columnNames = {"property", "transaction", "created_by"}
        )
    },
    indexes = {
        @Index(
            name = "idx_sale_transactions_created_by",
            columnList = "created_by"
        )
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "transaction",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_sale_transactions_transaction",
            foreignKeyDefinition = 
                "FOREIGN KEY (transaction) REFERENCES transactions(id) " +
                "ON DELETE RESTRICT ON UPDATE CASCADE"
        )
    )
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "property",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_sale_transactions_property",
            foreignKeyDefinition = 
                "FOREIGN KEY (property) REFERENCES properties(id) " +
                "ON DELETE RESTRICT ON UPDATE CASCADE"
        )
    )
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "contact",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_sale_transactions_contact",
            foreignKeyDefinition = 
                "FOREIGN KEY (contact) REFERENCES contacts(id) " +
                "ON DELETE RESTRICT ON UPDATE CASCADE"
        )
    )
    private Contact contact;

    @Column(name = "note", nullable = false, length = 255)
    private String note;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "created_by",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(
            name = "fk_sale_transactions_created_by",
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

    @OneToMany(mappedBy = "saleTransaction")
    private Set<SaleTransactionFileDetail> fileDetails = new HashSet<>();

}
