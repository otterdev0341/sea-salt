package com.otterdev.dto.ops.invest;

import java.time.LocalDateTime;
import java.util.List;

import com.otterdev.dto.ops.invest.item.ReqCreateUpdateInvestTransactionItemDto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreateInvestTransactionDto {
    @NotBlank(message = "Property note cannot be blank")
    private String note;

    @NotBlank(message = "invest item cannot be blank")
    private List<ReqCreateUpdateInvestTransactionItemDto> items;

    private LocalDateTime createdAt;
}
