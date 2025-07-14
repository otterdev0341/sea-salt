package com.otterdev.infrastructure.repository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.otterdev.domain.repository.ICloudflareR2Repository;
import com.otterdev.domain.valueObject.cloudflare.ResFileR2Dto;
import com.otterdev.utility.error.repository.DeleteFailedError;
import com.otterdev.utility.error.repository.FetchFailedError;
import com.otterdev.utility.error.repository.RepositoryError;
import com.otterdev.utility.error.repository.UploadFailedError;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@ApplicationScoped
public class CloudflareR2Repository implements ICloudflareR2Repository {

    private final S3Client s3Client;

    @ConfigProperty(name = "cloudflare.r2.bucket-name")
    String bucketName;

    @ConfigProperty(name = "cloudflare.r2.public-url-base", defaultValue = "")
    String publicUrlBase;

    @Inject
    public CloudflareR2Repository(
            @ConfigProperty(name = "cloudflare.r2.access-key-id") String accessKeyId,
            @ConfigProperty(name = "cloudflare.r2.secret-access-key") String secretAccessKey,
            @ConfigProperty(name = "cloudflare.r2.endpoint") String endpoint) {
            
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        this.s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .endpointOverride(URI.create(endpoint))
                .build();
    }

    @Override
    public Uni<Either<RepositoryError, ResFileR2Dto>> saveFile(String key, InputStream inputStream, long contentLength,
            String contentType) {
        return Uni.createFrom().item(() -> {
            try {
                PutObjectRequest putRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(contentType)
                        .build();

                s3Client.putObject(putRequest, RequestBody.fromInputStream(inputStream, contentLength));

                ResFileR2Dto response = new ResFileR2Dto();
                return Either.<RepositoryError, ResFileR2Dto>right(response);
            } catch (S3Exception e) {
                return Either.<RepositoryError, ResFileR2Dto>left(new UploadFailedError("Failed to save file: " + e.awsErrorDetails().errorMessage()));
            } catch (Exception e) {
                return Either.<RepositoryError, ResFileR2Dto>left(new UploadFailedError("Failed to save file: " + e.getMessage()));
            }
        });
    }

    @Override
    public Uni<Either<RepositoryError, Optional<ResFileR2Dto>>> getFile(String key) {
        return Uni.createFrom().item(() -> {
            try {
                GetObjectRequest getRequest = GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();

                ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getRequest);
                GetObjectResponse metadata = response.response();

                String fileUrl = generateFileUrl(key);
                ResFileR2Dto fileDto = new ResFileR2Dto(key, fileUrl, metadata.contentType(), metadata.contentLength());
                
                return Either.<RepositoryError, Optional<ResFileR2Dto>>right(Optional.of(fileDto));
            } catch (NoSuchKeyException e) {
                return Either.<RepositoryError, Optional<ResFileR2Dto>>right(Optional.empty());
            } catch (S3Exception e) {
                return Either.<RepositoryError, Optional<ResFileR2Dto>>left(new FetchFailedError("Failed to fetch file: " + e.awsErrorDetails().errorMessage()));
            } catch (Exception e) {
                return Either.<RepositoryError, Optional<ResFileR2Dto>>left(new FetchFailedError("Failed to fetch file: " + e.getMessage()));
            }
        });
    }

    @Override
    public Uni<Either<RepositoryError, List<ResFileR2Dto>>> getFiles(String prefix) {
        return Uni.createFrom().item(() -> {
            try {
                ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                        .bucket(bucketName)
                        .prefix(prefix)
                        .build();

                ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

                List<ResFileR2Dto> files = listResponse.contents().stream()
                        .map(obj -> {
                            String fileUrl = generateFileUrl(obj.key());
                            return new ResFileR2Dto(obj.key(), fileUrl, "application/octet-stream", obj.size());
                        })
                        .collect(Collectors.toList());

                return Either.<RepositoryError, List<ResFileR2Dto>>right(files);
            } catch (S3Exception e) {
                return Either.<RepositoryError, List<ResFileR2Dto>>left(new FetchFailedError("Failed to list files: " + e.awsErrorDetails().errorMessage()));
            } catch (Exception e) {
                return Either.<RepositoryError, List<ResFileR2Dto>>left(new FetchFailedError("Failed to list files: " + e.getMessage()));
            }
        });
    }

    @Override
public Uni<Either<RepositoryError, Void>> deleteFile(String key) {
    return Uni.createFrom().item(() -> {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteRequest);
            return Either.<RepositoryError, Void>right(null);
        } catch (S3Exception e) {
            return Either.<RepositoryError, Void>left(new DeleteFailedError("Failed to delete file: " + e.awsErrorDetails().errorMessage()));
        } catch (Exception e) {
            return Either.<RepositoryError, Void>left(new DeleteFailedError("Failed to delete file: " + e.getMessage()));
        }
    });
}





// Helper method to generate file URL
private String generateFileUrl(String key) {
    if (publicUrlBase != null && !publicUrlBase.isEmpty()) {
        // Use custom domain if configured
        return String.format("%s/%s", publicUrlBase, key);
    } else {
        // Use default R2 URL format
        return String.format("https://%s.r2.cloudflarestorage.com/%s", bucketName, key);
    }
}

@Override
public Uni<Either<RepositoryError, List<ResFileR2Dto>>> saveAllFiles(List<FileUpload> files) {
    return Uni.createFrom().item(() -> {
        try {
            // Validate input
            if (files == null || files.isEmpty()) {
                return Either.<RepositoryError, List<ResFileR2Dto>>left(
                    new UploadFailedError("No files provided for upload")
                );
            }

            List<ResFileR2Dto> results = new ArrayList<>();

            // Process each file
            for (FileUpload file : files) {
                try (InputStream inputStream = java.nio.file.Files.newInputStream(file.uploadedFile())) {
                    String key = file.fileName();
                    long contentLength = file.size();
                    String contentType = file.contentType();

                    PutObjectRequest putRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(contentType)
                        .build();

                    s3Client.putObject(putRequest, 
                        RequestBody.fromInputStream(inputStream, contentLength));

                    String fileUrl = generateFileUrl(key);
                    results.add(new ResFileR2Dto(key, fileUrl, contentType, contentLength));
                } catch (IOException e) {
                    return Either.<RepositoryError, List<ResFileR2Dto>>left(
                        new UploadFailedError("Failed to process file " + file.fileName() + ": " + e.getMessage())
                    );
                }
            }

            return Either.<RepositoryError, List<ResFileR2Dto>>right(results);

        } catch (S3Exception e) {
            return Either.<RepositoryError, List<ResFileR2Dto>>left(
                new UploadFailedError("Failed to save files to R2: " + e.awsErrorDetails().errorMessage())
            );
        } catch (Exception e) {
            return Either.<RepositoryError, List<ResFileR2Dto>>left(
                new UploadFailedError("Failed to save files: " + e.getMessage())
            );
        }
    });
}

@Override
public Uni<Either<RepositoryError, ResFileR2Dto>> saveFile(FileUpload file) {
    return Uni.createFrom().item(() -> {
        try {
            // Validate input
            if (file == null) {
                return Either.<RepositoryError, ResFileR2Dto>left(
                    new UploadFailedError("No file provided for upload")
                );
            }

            // Process the file
            try (InputStream inputStream = java.nio.file.Files.newInputStream(file.uploadedFile())) {
                String key = file.fileName();
                long contentLength = file.size();
                String contentType = file.contentType();

                PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .build();

                s3Client.putObject(putRequest, 
                    RequestBody.fromInputStream(inputStream, contentLength));

                String fileUrl = generateFileUrl(key);
                return Either.<RepositoryError, ResFileR2Dto>right(
                    new ResFileR2Dto(key, fileUrl, contentType, contentLength)
                );

            } catch (IOException e) {
                return Either.<RepositoryError, ResFileR2Dto>left(
                    new UploadFailedError("Failed to process file " + file.fileName() + ": " + e.getMessage())
                );
            }

        } catch (S3Exception e) {
            return Either.<RepositoryError, ResFileR2Dto>left(
                new UploadFailedError("Failed to save file to R2: " + e.awsErrorDetails().errorMessage())
            );
        } catch (Exception e) {
            return Either.<RepositoryError, ResFileR2Dto>left(
                new UploadFailedError("Failed to save file: " + e.getMessage())
            );
        }
    });
}

    @Override
    public Uni<Either<RepositoryError, Void>> deleteFiles(List<String> keys) {
        return Uni.createFrom().item(() -> {
            try {
                // Validate input
                if (keys == null || keys.isEmpty()) {
                    return Either.<RepositoryError, Void>left(
                        new DeleteFailedError("No keys provided for deletion")
                    );
                }

                // Create delete objects request
                List<ObjectIdentifier> objectIds = keys.stream()
                    .map(key -> ObjectIdentifier.builder().key(key).build())
                    .collect(Collectors.toList());

                Delete delete = Delete.builder()
                    .objects(objectIds)
                    .build();

                DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                    .bucket(bucketName)
                    .delete(delete)
                    .build();

                // Execute batch deletion
                DeleteObjectsResponse response = s3Client.deleteObjects(deleteRequest);

                // Check for errors
                if (response.hasErrors()) {
                    String errorMessages = response.errors().stream()
                        .map(error -> String.format("Key: %s, Error: %s", 
                            error.key(), error.message()))
                        .collect(Collectors.joining(", "));
                    
                    return Either.<RepositoryError, Void>left(
                        new DeleteFailedError("Failed to delete some files: " + errorMessages)
                    );
                }

                return Either.<RepositoryError, Void>right(null);

            } catch (S3Exception e) {
                return Either.<RepositoryError, Void>left(
                    new DeleteFailedError("Failed to delete files from R2: " + 
                        e.awsErrorDetails().errorMessage())
                );
            } catch (Exception e) {
                return Either.<RepositoryError, Void>left(
                    new DeleteFailedError("Failed to delete files: " + e.getMessage())
                );
            }
        });
    }
}
