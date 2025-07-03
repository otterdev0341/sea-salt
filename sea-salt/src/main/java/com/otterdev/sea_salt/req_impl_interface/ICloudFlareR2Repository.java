package com.otterdev.sea_salt.req_impl_interface;

import java.nio.ByteBuffer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICloudFlareR2Repository {
    
    Mono<Void> saveFile(String key, Flux<ByteBuffer> content, long contentLength, String contentType);

    Mono<String> getFile(String key); // returns file content or a reference, adjust as needed

    Flux<String> getFiles(String prefix); // returns list of file keys under a prefix

    Mono<Void> deleteFile(String key);

    Mono<Boolean> isFileExist(String key);
}
