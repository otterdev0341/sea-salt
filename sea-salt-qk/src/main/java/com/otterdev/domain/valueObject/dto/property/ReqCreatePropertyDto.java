package com.otterdev.domain.valueObject.dto.property;

import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreatePropertyDto {
    
    @RestForm("name")
    @PartType(MediaType.TEXT_PLAIN)
    @NotBlank(message = "Property name cannot be blank")
    private String name;

    @RestForm("description")
    @PartType(MediaType.TEXT_PLAIN)
    private String description;

    @RestForm("specific")
    @PartType(MediaType.TEXT_PLAIN)
    private String specific;

    @RestForm("highlight")
    @PartType(MediaType.TEXT_PLAIN)
    private String hilight;

    @RestForm("area")
    @PartType(MediaType.TEXT_PLAIN)
    private String area;

    @RestForm("price")
    @PartType(MediaType.TEXT_PLAIN)
    private String price; 

    @RestForm("fsp")
    @PartType(MediaType.TEXT_PLAIN)
    private String fsp;

    @RestForm("status")
    @PartType(MediaType.TEXT_PLAIN)
    private String status;

    @RestForm("ownerBy")
    @PartType(MediaType.TEXT_PLAIN)
    @NotNull(message = "Owner by is required")
    private String ownerBy;

    @RestForm("mapUrl")
    @PartType(MediaType.TEXT_PLAIN)
    private String mapUrl;

    @RestForm("lat")
    @PartType(MediaType.TEXT_PLAIN)
    private String lat;

    @RestForm("lng")
    @PartType(MediaType.TEXT_PLAIN)
    private String lng;

    @RestForm("files")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private List<FileUpload> files;
    
    public UUID getOwnerByAsUUID() {
        return UUID.fromString(this.getOwnerBy());
    }

    public UUID getPropertyStatusAsUUID() {
        return UUID.fromString(this.getStatus());
    }

    public Double getPriceAsDouble() {
        try {
            return Double.parseDouble(this.getPrice());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid price format: " + this.getPrice());
        }
    }

    public Double getFspAsDouble() {
        try {
            return Double.parseDouble(this.getFsp());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid FSP format: " + this.getFsp());
        }
    }
}
