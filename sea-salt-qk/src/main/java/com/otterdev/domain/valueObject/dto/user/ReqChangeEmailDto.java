package com.otterdev.domain.valueObject.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqChangeEmailDto {
    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Old email is required")
    private String oldEmail;

    @Email(message = "Invalid email format")
    @NotBlank(message = "New email is required")
    private String newEmail;

    @NotBlank(message = "Password is required")
    private String password;
}
