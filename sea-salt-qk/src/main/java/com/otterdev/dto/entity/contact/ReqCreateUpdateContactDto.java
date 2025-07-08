package com.otterdev.dto.entity.contact;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqCreateUpdateContactDto {

    @NotBlank(message = "Business name is required")
    private String businessName;

    private String internalName;

    private String detail;

    private String note;

    @NotNull(message = "Contact type ID is required")
    private UUID contactTypeId;

    private String address;

    private String phone;

    private String mobilePhone;

    private String line;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
}
