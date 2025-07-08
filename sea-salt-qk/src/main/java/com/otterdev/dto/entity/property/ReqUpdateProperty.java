package com.otterdev.dto.entity.property;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqUpdateProperty {
    @NotBlank(message = "ID cannot be blank")
    private UUID id;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    private String description;
    private String specific;
    private String hilight;
    private String area;
    private Double price;
    private Double fsp;
    @NotBlank(message = "Property type cannot be blank")
    private UUID status;
    private String mapUrl;
    private String lat;
    private String lng;
}
