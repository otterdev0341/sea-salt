package com.otterdev.domain.valueObject.dto.propertyStatus;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqUpdatePropertyStatusDto {
    @NotBlank(message = "Property status to update cannot be blank")
    private String detail;
}
