package com.otterdev.dto.ops.payment.item;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ResEntryPaymentTransactionItemDto {
    private UUID id;
    private UUID paymentTransactionId;
    private Double amount;
    private Double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
