package com.otterdev.dto.entity.contactType;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreateUpdateContactTypeDto {
    
    @NotBlank(message = "Description cannot be blank")
    private String description;
}
