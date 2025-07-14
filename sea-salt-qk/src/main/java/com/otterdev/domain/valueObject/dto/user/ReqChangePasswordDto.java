package com.otterdev.domain.valueObject.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqChangePasswordDto {
    
    @NotBlank(message = "New password is required")
    private String newPassword; 

    @NotBlank(message = "Verify new password is required")
    private String verifyNewPassword;

    @NotBlank(message = "Old password is required")
    private String oldPassword;

}
