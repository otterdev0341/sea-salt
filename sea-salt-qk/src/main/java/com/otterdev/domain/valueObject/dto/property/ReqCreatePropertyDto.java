package com.otterdev.domain.valueObject.dto.property;

import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.FormParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreatePropertyDto {
    
    @FormParam("name")
    @NotBlank(message = "Property name cannot be blank")
    private String name;

    @FormParam("description")
    private String description;

    @FormParam("specific")
    private String specific;

    @FormParam("highlight")
    private String hilight;

    @FormParam("area")
    private String area;

    @FormParam("price")
    private Double price; 

    @FormParam("fsp")
    private Double fsp;

    @FormParam("propertyType")
    private UUID status;

    @FormParam("ownerBy")
    @NotBlank(message = "Owner by is required")
    private UUID ownerBy;

    @FormParam("mapUrl")
    private String mapUrl;

    @FormParam("lat")
    private String lat;

    @FormParam("lng")
    private String lng;

    @FormParam("files")
    private List<FileUpload> files;
    

}
