package com.otterdev.domain.valueObject.dto.user;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqChangeUserInfoDto {
    
    @NotBlank(message = "first name is required")
    private String firstName;

    @NotBlank(message = "last name is required")
    private String lastName;

    @NotBlank(message = "date of birth is required")
    private String dob;

    @NotBlank(message = "gender is required")
    private UUID gender;

    @NotBlank(message = "Password is required")
    private String password;
}
