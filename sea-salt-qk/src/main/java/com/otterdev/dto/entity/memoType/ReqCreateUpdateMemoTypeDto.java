package com.otterdev.dto.entity.memoType;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreateUpdateMemoTypeDto {
    @NotBlank(message = "Detail cannot be blank")
    private String detail;
}
