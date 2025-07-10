package com.otterdev.repository.cloudflare;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.otterdev.Iinterface.ICloudflareR2Repository;
import com.otterdev.dto.cloudflare.ResFileR2Dto;
import com.otterdev.error.repository.DeleteFailedError;
import com.otterdev.error.repository.FetchFailedError;
import com.otterdev.error.repository.RepositoryError;
import com.otterdev.error.repository.UploadFailedError;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
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
}
