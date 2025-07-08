package com.otterdev.dto.entity.expense;

import java.time.LocalDateTime;
import java.util.UUID;

import com.otterdev.dto.entity.expenseType.ResEntryExpenseTypeDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResEntryExpenseDto {
    
    private UUID id;
    
    private String detail;
    
    private ResEntryExpenseTypeDto expenseType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
