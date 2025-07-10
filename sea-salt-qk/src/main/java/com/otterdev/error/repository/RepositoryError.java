package com.otterdev.error.repository;

public sealed interface RepositoryError permits NotFoundError, UploadFailedError, DeleteFailedError, FetchFailedError {
    String message();
}




