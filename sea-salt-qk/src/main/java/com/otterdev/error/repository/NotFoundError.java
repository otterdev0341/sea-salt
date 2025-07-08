package com.otterdev.error.repository;

public record NotFoundError(String message) implements RepositoryError {}