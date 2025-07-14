package com.otterdev.utility.error.repository;
public record DeleteFailedError(String message) implements RepositoryError {}
