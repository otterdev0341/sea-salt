package com.otterdev.domain.valueObject.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqChangeUsernameDto {
    
    @NotBlank(message = "Current username is required")
    private String newUsername;

}
