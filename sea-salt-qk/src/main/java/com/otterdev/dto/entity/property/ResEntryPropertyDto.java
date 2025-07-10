package com.otterdev.dto.entity.property;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResEntryPropertyDto {
    private UUID id;
    private String name;
    private String description;
    private String specific;
    private String hilight;
    private String area;
    private Double price;
    private Double fsp;
    private UUID status;
    private String mapUrl;
    private String lat;
    private String lng;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
