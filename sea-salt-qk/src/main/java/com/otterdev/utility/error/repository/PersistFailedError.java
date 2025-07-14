package com.otterdev.utility.error.repository;

public record PersistFailedError(String message) implements RepositoryError {}
