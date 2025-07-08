package com.otterdev.entity.transaction;

import java.util.UUID;

import com.otterdev.entity.table.Expense;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name= "payment_transaction_item")
public class PaymentTransactionItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "payment_transaction_id", referencedColumnName = "id", nullable=false)
    private PaymentTransaction paymentTransactionId;

    @ManyToOne
    @JoinColumn(name = "expense_id", referencedColumnName = "id", nullable = false)
    private Expense expenseId;

    @Column(name= "amount")
    private Double amount;

    @Column(name= "price")
    private Double price;
}
