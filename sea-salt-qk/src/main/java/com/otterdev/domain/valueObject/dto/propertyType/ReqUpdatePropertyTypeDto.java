package com.otterdev.domain.valueObject.dto.propertyType;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqUpdatePropertyTypeDto {
    
    @NotBlank(message = "Property type name cannot be blank")
    private String detail;
}

