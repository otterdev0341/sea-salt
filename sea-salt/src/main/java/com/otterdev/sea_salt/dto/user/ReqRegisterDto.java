package com.otterdev.sea_salt.dto.user;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReqRegisterDto {
    private String email;
    private String password;
    private String username;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dob;
}
