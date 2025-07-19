package com.otterdev.domain.valueObject.dto.expense;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqUpdateExpenseDto {
    
    @NotBlank(message = "Expense detail is required")
    private String detail;

    @NotNull(message = "Expense type id is required")
    private UUID expenseType;
}
