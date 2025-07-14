package com.otterdev.domain.valueObject.cloudflare;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResFileR2Dto {
    private String objectKey;
    private String fileUrl;
    private String contentType;
    private Long contentLength;
}
