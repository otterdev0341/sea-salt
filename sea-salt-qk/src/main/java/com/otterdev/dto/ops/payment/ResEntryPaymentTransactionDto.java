package com.otterdev.dto.ops.payment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.otterdev.dto.ops.payment.item.ResEntryPaymentTransactionItemDto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ResEntryPaymentTransactionDto {
    private UUID id;
    private UUID transactionId;
    private String note;
    private List<ResEntryPaymentTransactionItemDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
