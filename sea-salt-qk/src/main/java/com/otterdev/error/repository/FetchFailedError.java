package com.otterdev.error.repository;

public record FetchFailedError(String message) implements RepositoryError {}
