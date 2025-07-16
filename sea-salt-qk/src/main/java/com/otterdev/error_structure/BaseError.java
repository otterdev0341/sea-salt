package com.otterdev.error_structure;

public sealed interface BaseError 
    permits RepositoryError, ServiceError, UsecaseError {
    String message();
    String code();
    ErrorType type();
}
