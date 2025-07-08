package com.otterdev.error.service;

public record ResourceNotFoundError(String message) implements ServiceError {}
// This class represents a resource not found error in the service layer.
