package com.otterdev.dto.entity.propertyType;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreateUpdatePropertyType {
    @NotBlank(message = "Property type detail cannot be null")
    private String detail;
}
