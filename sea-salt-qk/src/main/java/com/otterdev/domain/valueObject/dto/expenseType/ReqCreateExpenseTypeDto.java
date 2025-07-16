package com.otterdev.domain.valueObject.dto.expenseType;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreateExpenseTypeDto {
    
    @NotBlank(message = "detail can not be blank")
    private String detail;
    
}
