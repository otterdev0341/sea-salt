package com.otterdev.infrastructure.repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.otterdev.domain.valueObject.cloudflare.ResFileR2Dto;
import com.otterdev.error_structure.RepositoryError;
import com.otterdev.infrastructure.repository.internal.CloudFlareR2Repository;
import com.spencerwi.either.Either;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@ApplicationScoped
public class CloudFlareR2RepositoryImpl implements CloudFlareR2Repository {

    private final S3Client s3Client;
    private final String bucketName;
    private final String publicUrlBase;

    @Inject
    public CloudFlareR2RepositoryImpl(
            @ConfigProperty(name = "cloudflare.r2.access-key-id") String accessKeyId,
            @ConfigProperty(name = "cloudflare.r2.secret-access-key") String secretAccessKey,
            @ConfigProperty(name = "cloudflare.r2.endpoint") String endpoint,
            @ConfigProperty(name = "cloudflare.r2.bucket-name") String bucketName,
            @ConfigProperty(name = "cloudflare.r2.public-url-base") String publicUrlBase) {
        
        this.bucketName = bucketName;
        this.publicUrlBase = publicUrlBase;
        
        this.s3Client = S3Client.builder()
            .endpointOverride(java.net.URI.create(endpoint))
            .region(Region.of("auto"))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKeyId, secretAccessKey)
            ))
            .build();
    }

    @Override
    public Uni<Either<RepositoryError, ResFileR2Dto>> uploadFile(FileUpload file) {
        return Uni.createFrom().item(() -> {
            try {
                String uniqueFileName = generateUniqueFileName(file.fileName());
                Path tempFile = createTempFile(file);

                uploadToR2(tempFile, uniqueFileName, file.contentType());
                Files.delete(tempFile);
                System.out.println("Temp file path: " + tempFile);
                return Either.right(createFileResponse(uniqueFileName, file));
            } catch (Exception e) {
                return Either.left(new RepositoryError.PersistenceFailed(
                    "Failed to upload file: " + e.getMessage()));
            }
        });
    }

    @Override
    public Uni<Either<RepositoryError, ResFileR2Dto>> getFile(String objectKey) {
        return Uni.createFrom().item(() -> {
            try {
                var metadata = s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build());

                ResFileR2Dto response = new ResFileR2Dto();
                response.setObjectKey(objectKey);
                response.setContentType(metadata.contentType());
                response.setContentLength(metadata.contentLength());
                response.setFileUrl(publicUrlBase + "/" + objectKey);

                return Either.right(response);
            } catch (S3Exception e) {
                if (e.statusCode() == 404) {
                    return Either.left(new RepositoryError.NotFound("File not found: " + objectKey));
                }
                return Either.left(new RepositoryError.FetchFailed(
                    "Failed to get file: " + e.getMessage()));
            }
        });
    }

    @Override
    public Uni<Either<RepositoryError, Boolean>> deleteFile(String bucketName, String filePath) {
        return Uni.createFrom().item(() -> {
            try {
                DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filePath)
                    .build();
                
                s3Client.deleteObject(deleteRequest);
                return Either.right(true);
            } catch (S3Exception e) {
                if (e.statusCode() == 404) {
                    return Either.left(new RepositoryError.NotFound("File not found: " + filePath));
                }
                return Either.left(new RepositoryError.DeleteFailed(
                    "Failed to delete file: " + e.getMessage()));
            }
        });
    }

    // Helper methods
    private String generateUniqueFileName(String originalFileName) {
        String extension = originalFileName.contains(".") 
            ? originalFileName.substring(originalFileName.lastIndexOf("."))
            : "";
        return UUID.randomUUID().toString() + extension;
    }

    private Path createTempFile(FileUpload file) throws Exception {
        Path tempFile = Files.createTempFile("upload", null);
        Files.copy(file.filePath(), tempFile, StandardCopyOption.REPLACE_EXISTING);
        return tempFile;
    }

    private void uploadToR2(Path file, String fileName, String contentType) {
        PutObjectRequest putRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .contentType(contentType)
            .build();

        s3Client.putObject(putRequest, RequestBody.fromFile(file.toFile()));
    }

    private ResFileR2Dto createFileResponse(String fileName, FileUpload originalFile) {
        ResFileR2Dto response = new ResFileR2Dto();
        response.setFileName(fileName);
        response.setObjectKey(fileName);
        response.setContentType(originalFile.contentType());
        response.setContentLength(originalFile.size());
        response.setFileUrl(publicUrlBase + "/" + fileName);
        return response;
    }
}
