package com.otterdev.dto.entity.expenseType;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqCreateUpdateExpenseTypeDto {
    @NotBlank(message = "detail cannot be blank")
    private String detail;
}
