package com.otterdev.domain.valueObject.dto.propertyStatus;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreatePropertyStatusDto {
    
    @NotBlank(message = "Property status name cannot be blank")
    private String detail;
    
}
