package com.otterdev.sea_salt.soc.serviceError;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
    
}
