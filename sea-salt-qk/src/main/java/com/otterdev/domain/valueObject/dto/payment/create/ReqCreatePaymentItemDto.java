package com.otterdev.domain.valueObject.dto.payment.create;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.FormParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreatePaymentItemDto {
    
    @FormParam("expense")
    @NotNull(message = "Expense cannot be null")
    private UUID expense;

    @FormParam("amount")
    @Min(value = 0, message = "Amount must be a positive number")
    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be a positive number")
    private Double amount;

    @FormParam("price")
    @Min(value = 0, message = "Price must be a positive number")
    @NotNull(message = "Price cannot be null")
    private Double price;


}
