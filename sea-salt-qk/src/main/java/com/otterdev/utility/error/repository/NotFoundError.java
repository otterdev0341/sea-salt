package com.otterdev.utility.error.repository;

public record NotFoundError(String message) implements RepositoryError {}