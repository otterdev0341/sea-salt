package com.otterdev.dto.ops.invest.item;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreateUpdateInvestTransactionItemDto {
    @NotBlank(message = "Property ID cannot be blank")
    private UUID propertyId;
    @NotBlank(message = "Contact ID cannot be blank")
    private UUID contactId;
    @NotBlank(message = "Note cannot be blank")
    @Min(value = 1, message = "Note must be at least 1 character long")
    private Double amount;
    @NotBlank(message = "Funding Percent cannot be blank")
    private Double fundingPercent;
}
