package com.otterdev.dto.entity.propertyStatus;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreateUpdatePropertyStatusDto {
    
    @NotBlank(message = "Property status name cannot be null")
    private String detail;
}
