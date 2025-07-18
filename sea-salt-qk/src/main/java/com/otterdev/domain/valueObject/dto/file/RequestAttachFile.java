package com.otterdev.domain.valueObject.dto.file;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAttachFile {
    
    @RestForm("file")
    @NotNull(message = "File must not be null")
    private FileUpload file;
}
