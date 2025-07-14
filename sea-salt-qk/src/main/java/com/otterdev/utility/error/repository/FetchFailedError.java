package com.otterdev.utility.error.repository;

public record FetchFailedError(String message) implements RepositoryError {}
