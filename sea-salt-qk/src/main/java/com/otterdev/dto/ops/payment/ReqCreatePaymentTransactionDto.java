package com.otterdev.dto.ops.payment;


import java.time.LocalDateTime;
import java.util.List;

import com.otterdev.dto.ops.payment.item.ReqCreateUpdatePaymentTransactionItemDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreatePaymentTransactionDto {
    @NotBlank(message = "Note cannot be blank")
    private String note;

    @NotBlank(message = "Property ID cannot be blank")
    private List<ReqCreateUpdatePaymentTransactionItemDto> item;

    @NotBlank(message = "Created At cannot be blank")
    private LocalDateTime createdAt;

    @NotBlank(message = "Updated At cannot be blank")
    private LocalDateTime updatedAt;
}
