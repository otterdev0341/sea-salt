package com.otterdev.sea_salt.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property {
    @Id
    private UUID id;
    
    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("specific")
    private String specific;

    @Column("hilight")
    private String hilight;

    @Column("area")
    private String area;

    @Column("price")
    private Double price;

    @Column("f_s_p")
    private Double fsp; // mapped from `f_s_p` (adjusted field name)

    @Column("status")
    private UUID status;

    @Column("owner_by")
    private UUID ownerBy;

    @Column("map_usl")
    private String mapUrl;

    @Column("lat")
    private String lat;

    @Column("long")
    private String lng; // mapped from `long` to avoid Java keyword conflict

    @Column("sold")
    private Boolean sold;

    @Column("created_by")
    private UUID createdBy;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
