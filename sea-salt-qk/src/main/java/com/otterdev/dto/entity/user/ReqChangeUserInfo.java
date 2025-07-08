package com.otterdev.dto.entity.user;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReqChangeUserInfo {
    
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String gender;
    private String password;
}
