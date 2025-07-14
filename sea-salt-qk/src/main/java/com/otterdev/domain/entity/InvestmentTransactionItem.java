package com.otterdev.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "invest_transaction_items",
    indexes = {
        @Index(
            name = "idx_invest_transaction_items_invest_transaction",
            columnList = "invest_transaction"
        ),
        @Index(
            name = "idx_invest_transaction_items_created_by",
            columnList = "created_by"
        )
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvestmentTransactionItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "invest_transaction",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_invest_transaction_items_transaction",
            foreignKeyDefinition = 
                "FOREIGN KEY (invest_transaction) REFERENCES invest_transactions(id) " +
                "ON DELETE RESTRICT ON UPDATE CASCADE"
        )
    )
    private InvestmentTransaction investTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "contact",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_invest_transaction_items_contact",
            foreignKeyDefinition = 
                "FOREIGN KEY (contact) REFERENCES contacts(id) " +
                "ON DELETE RESTRICT ON UPDATE CASCADE"
        )
    )
    private Contact contact;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "created_by",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(
            name = "fk_invest_transaction_items_created_by",
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
