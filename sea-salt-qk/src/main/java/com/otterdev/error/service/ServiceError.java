package com.otterdev.error.service;

public sealed interface ServiceError permits ValidationError, ResourceNotFoundError, UnexpectedError{
    String message();
}
