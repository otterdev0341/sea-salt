package com.otterdev.error.service;

public record UnexpectedError(String message, Throwable cause) implements ServiceError {
    public UnexpectedError(String message) {
        this(message, null);
    }
}
