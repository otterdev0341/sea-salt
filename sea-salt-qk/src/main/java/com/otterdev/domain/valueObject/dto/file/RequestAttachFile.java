package com.otterdev.domain.valueObject.dto.file;

import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAttachFile {
    
    @RestForm("file")
    @NotNull(message = "File must not be null")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private FileUpload file;
}
