package com.otterdev.domain.valueObject.dto.memo;

import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreateMemoDto {
    
    @RestForm("name")
    @NotBlank(message = "name of memo is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @RestForm("detail")
    private String detail;

    @RestForm("memoType")
    @NotBlank(message = "Memo type is required")
    private UUID memoType;

    @RestForm("files")
    @Size(max = 7, message = "Maximum 7 files allowed")
    private List<FileUpload> files;
}
