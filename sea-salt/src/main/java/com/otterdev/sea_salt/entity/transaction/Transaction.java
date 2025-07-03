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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("transaction")
public class Transaction {
    
    @Id
    private UUID id;

    @Column("note")
    private String note;

    @Column("transaction_type_id")
    private UUID transactionTyepId;

    @Column("property_id")
    private UUID propertyId;

    @Column("user_id")
    private UUID userId;

    @Column("created_at")
    private LocalDateTime createdAt;
}
