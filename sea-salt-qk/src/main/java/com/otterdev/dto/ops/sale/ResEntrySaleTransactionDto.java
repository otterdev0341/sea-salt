package com.otterdev.dto.ops.sale;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResEntrySaleTransactionDto {
    private UUID propertyId;
    private UUID contactId;
    private UUID note;
    private Double amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
