package com.otterdev.sea_salt.entity.join;

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
@Table("file_user")
public class FileUser {
    
    @Column("id")
    private UUID id;

    @Column("file_id")
    private UUID fileId;

    @Column("user_id")
    private UUID userId;

    @Column("file_type_id")
    private UUID fileTypeID;
}
