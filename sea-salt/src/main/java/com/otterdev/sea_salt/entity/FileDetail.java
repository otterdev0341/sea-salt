package com.otterdev.sea_salt.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("file_detail")
public class FileDetail {

    @Id
    private UUID id;

    @Column("object_key")
    private String objectKey;

    @Column("url")
    private String url;

    @Column("content_type")
    private String contentType;

    @Column("size")
    private String size;

    @Column("uploaded_by")
    private UUID uploadedBy;

    @Column("uploaded_at")
    private LocalDateTime uploadedAt;
}
