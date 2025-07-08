package com.otterdev.error.repository;

public record DeleteFailedError(String message) implements RepositoryError {}
