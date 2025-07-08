package com.otterdev.dto.ops.invest.item;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResEntryInvestTransactionItemDto {
    private UUID id;
    private UUID investTransactionId;
    private UUID propertyId;
    private UUID contactId;
    private Double amount;
    private Double fundingPercent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
