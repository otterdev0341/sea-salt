package com.otterdev.dto.ops.invest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.otterdev.dto.ops.invest.item.ResEntryInvestTransactionItemDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResEntryInvestTransactionDto {
    private UUID id;
    private UUID transactionId;
    private String note;
    private List<ResEntryInvestTransactionItemDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
