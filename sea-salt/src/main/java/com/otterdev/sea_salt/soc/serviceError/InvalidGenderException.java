package com.otterdev.sea_salt.soc.serviceError;

public class InvalidGenderException extends RuntimeException {
    public InvalidGenderException(String message) {
        super(message);
    }
}
