package com.otterdev.utility.error.service;

public record ValidationError(String message) implements ServiceError {}
// This class represents a validation error in the service layer.
