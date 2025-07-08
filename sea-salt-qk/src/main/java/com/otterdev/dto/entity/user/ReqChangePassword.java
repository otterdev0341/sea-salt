package com.otterdev.dto.entity.user;

import lombok.Data;

@Data
public class ReqChangePassword {
    private String oldPassword;
    private String newPassword;


}
