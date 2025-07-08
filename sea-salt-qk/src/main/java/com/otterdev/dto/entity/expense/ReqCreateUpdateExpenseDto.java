package com.otterdev.dto.entity.expense;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreateUpdateExpenseDto {
    @NotBlank(message = "Detail cannot be blank")
    private String detail;
    @NotBlank(message = "expense type id cannot be blank")
    private UUID expenseType;
}
