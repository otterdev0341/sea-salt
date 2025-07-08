package com.otterdev.dto.entity.user;

import lombok.Data;

@Data
public class ReqChangeEmail {
    
    private String oldEmail;
    private String newEmail;
    private String password; // Password for verification

 
}
