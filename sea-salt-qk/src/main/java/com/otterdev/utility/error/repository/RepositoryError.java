package com.otterdev.utility.error.repository;

public sealed interface RepositoryError permits NotFoundError, UploadFailedError, DeleteFailedError, FetchFailedError, PersistFailedError {
    String message();
}




