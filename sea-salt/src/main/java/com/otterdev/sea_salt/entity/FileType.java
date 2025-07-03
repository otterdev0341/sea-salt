package com.otterdev.sea_salt.entity;

import java.util.UUID;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("file_type")
public class FileType {
    
    @Column("id")
    private UUID id;

    @Column("description")
    private String description;
}
