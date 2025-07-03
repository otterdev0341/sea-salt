package com.otterdev.sea_salt.dto.user;

import lombok.Data;

@Data
public class ReqChangePassword {
    private String oldPassword;
    private String newPassword;


}
