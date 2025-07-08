package com.otterdev.dto.ops.sale;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreateUpdateSaleTransactionDto {

    @NotBlank(message = "Property ID cannot be blank")
    private UUID propertyId;
    
    @NotBlank(message = "Contact ID cannot be blank")
    private UUID contactId;
    
    @NotBlank(message = "Note cannot be blank")
    private UUID note;
    
    @NotBlank(message = "Amount cannot be blank")
    @Min(value = 1, message = "Amount must be at least 1")
    private Double amount;
    
    @NotBlank(message = "Created At cannot be blank")
    private LocalDateTime createdAt;
}
