package com.otterdev.utility.error.repository;

public record UploadFailedError(String message) implements RepositoryError {}