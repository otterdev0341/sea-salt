package com.otterdev.domain.valueObject.dto.memoType;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreateMemoTypeDto {
    
    @NotBlank(message = "Memo type detail is required")
    private String detail;
}
