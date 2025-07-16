package com.otterdev.domain.valueObject.dto.expense;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreateExpenseDto {
    
    @NotBlank(message = "Expense detail is required")
    private String detail;

    @NotBlank(message = "Expense type id is required")
    private UUID expenseType;
}
