package com.otterdev.domain.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.otterdev.domain.entity.relation.PaymentTransactionFileDetail;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "payment_transactions",
    indexes = {
        @Index(
            name = "idx_payment_transactions_created_by",
            columnList = "created_by"
        )
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "transaction",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_payment_transactions_transaction",
            foreignKeyDefinition = 
                "FOREIGN KEY (transaction) REFERENCES transactions(id) " +
                "ON DELETE RESTRICT ON UPDATE CASCADE"
        )
    )
    private Transaction transaction;

    @Column(name = "note", nullable = false, length = 255)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "property",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_payment_transactions_property",
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
            name = "fk_payment_transactions_contact",
            foreignKeyDefinition = 
                "FOREIGN KEY (contact) REFERENCES contacts(id) " +
                "ON DELETE RESTRICT ON UPDATE CASCADE"
        )
    )
    private Contact contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "created_by",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(
            name = "fk_payment_transactions_created_by",
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

    @OneToMany(mappedBy = "paymentTransaction")
    private Set<PaymentTransactionFileDetail> fileDetails = new HashSet<>();


} // end class
