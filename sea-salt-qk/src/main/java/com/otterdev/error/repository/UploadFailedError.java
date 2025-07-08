package com.otterdev.error.repository;

public record UploadFailedError(String message) implements RepositoryError {}