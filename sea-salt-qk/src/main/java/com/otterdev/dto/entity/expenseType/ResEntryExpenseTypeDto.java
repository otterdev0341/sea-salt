package com.otterdev.dto.entity.expenseType;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResEntryExpenseTypeDto {
    
    private UUID id;
    private String detail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
