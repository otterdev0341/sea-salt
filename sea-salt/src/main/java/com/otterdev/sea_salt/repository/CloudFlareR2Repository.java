package com.otterdev.sea_salt.repository;

import java.net.URI;
import java.nio.ByteBuffer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.otterdev.sea_salt.req_impl_interface.ICloudFlareR2Repository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

@Repository
@RequiredArgsConstructor
public class CloudFlareR2Repository implements ICloudFlareR2Repository {

    @Value("${r2.access-key}")
    private String accessKey;

    @Value("${r2.secret-key}")
    private String secretKey;

    @Value("${r2.bucket}")
    private String bucket;

    @Value("${r2.account-id}")
    private String accountId;

    @Value("${r2.region:auto}")
    private String region;

    private S3AsyncClient s3Client;

    @PostConstruct
    public void init() {
        this.s3Client = S3AsyncClient.builder()
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)))
            .endpointOverride(URI.create("https://" + accountId + ".r2.cloudflarestorage.com"))
            .region(Region.of(region))
            .build();
    }

    @Override
    public Mono<Void> saveFile(String key, Flux<ByteBuffer> content, long contentLength, String contentType) {
        PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentLength(contentLength)
            .contentType(contentType)
            .build();

        return Mono.fromFuture(s3Client.putObject(request, AsyncRequestBody.fromPublisher(content)))
                .then();
    }

    @Override
    public Mono<String> getFile(String key) {
        GetObjectRequest request = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

        return Mono.fromFuture(s3Client.getObject(request, AsyncResponseTransformer.toBytes()))
            .map(ResponseBytes::asUtf8String);
    }

    @Override
    public Flux<String> getFiles(String prefix) {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
            .bucket(bucket)
            .prefix(prefix)
            .build();

        return Mono.fromFuture(s3Client.listObjectsV2(request))
            .flatMapMany(response -> Flux.fromIterable(response.contents()))
            .map(S3Object::key);
    }

    @Override
    public Mono<Void> deleteFile(String key) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

        return Mono.fromFuture(s3Client.deleteObject(request)).then();
    }

    @Override
    public Mono<Boolean> isFileExist(String key) {
        HeadObjectRequest request = HeadObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

        return Mono.fromFuture(s3Client.headObject(request))
            .map(response -> true)
            .onErrorResume(NoSuchKeyException.class, e -> Mono.just(false));
    }
}
