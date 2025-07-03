package com.otterdev.sea_salt.entity.transaction;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("payment_transaction_item")
public class PaymentTransactionItem {
    
    @Id
    private UUID id;

    @Column("payment_transaction_id")
    private UUID paymentTransactionId;

    @Column("expense_id")
    private UUID expenseId;

    @Column("amount")
    private Double amount;

    @Column("price")
    private Double price;
}
