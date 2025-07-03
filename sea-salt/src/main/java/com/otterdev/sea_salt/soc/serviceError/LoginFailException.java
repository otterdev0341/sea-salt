package com.otterdev.sea_salt.soc.serviceError;

public class LoginFailException extends RuntimeException {
    public LoginFailException(String message) {
        super(message);
    }
    
}
