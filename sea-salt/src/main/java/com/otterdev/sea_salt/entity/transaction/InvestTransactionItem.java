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
@Table("invest_transaction_item")
public class InvestTransactionItem {
    
    @Id
    private UUID id;

    @Column("invest_transaction_id")
    private UUID investTransactionId;

    @Column("property_id")
    private UUID propertyId;

    @Column("contact_id")
    private UUID contactId;

    @Column("amount")
    private Double amount;

    @Column("funding_percent")
    private Double fundingPercent;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

}
