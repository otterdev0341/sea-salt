package com.otterdev.sea_salt.entity.transaction;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("sale_transaction")
public class SaleTransaction {
    
    @Id
    private UUID id;

    @Column("transaction_id")
    private UUID transactionId;

    @Column("property_id")
    private UUID propertyId;

    @Column("contact_id")
    private UUID contactId;

    @Column("note")
    private String note;

    @Column("user_id")
    private UUID userId;

    @Column("amount")
    private Double amount;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
