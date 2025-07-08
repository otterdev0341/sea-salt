package com.otterdev.entity.table;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "file_detail", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"object_key", "uploaded_by"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name= "object_key")
    private String objectKey;

    @Column(name= "url")
    private String url;

    @Column(name= "content_type")
    private String contentType;

    @Column(name= "size")
    private String size;

    @ManyToOne
    @JoinColumn(name = "uploaded_by", referencedColumnName = "id", nullable = false)
    private User uploadedBy;

    @Column(name= "uploaded_at")
    private LocalDateTime uploadedAt;
}


