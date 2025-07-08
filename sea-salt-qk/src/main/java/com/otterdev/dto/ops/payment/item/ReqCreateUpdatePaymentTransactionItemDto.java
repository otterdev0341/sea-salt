package com.otterdev.dto.ops.payment.item;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreateUpdatePaymentTransactionItemDto {
    @NotBlank(message = "Expense ID cannot be blank")
    private UUID exptenseId;
    
    @NotBlank(message = "Contact ID cannot be blank")
    @Min(value = 1, message = "Contact ID must be at least 1 character long")
    private Double amount;
    
    @NotBlank(message = "Note cannot be blank")
    @Min(value = 1, message = "Note must be at least 1 character")
    private Double price;
}
