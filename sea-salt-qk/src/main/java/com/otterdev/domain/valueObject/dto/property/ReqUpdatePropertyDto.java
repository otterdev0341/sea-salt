package com.otterdev.domain.valueObject.dto.property;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqUpdatePropertyDto {
    
    
    @NotBlank(message = "Property name cannot be blank")
    private String name;

    private String description;

    
    private String specific;

    
    private String hilight;

    
    private String area;

    
    private Double price; 

    
    private Double fsp;

    
    @NotNull(message = "Status cannot be null")
    private UUID status;

    @NotBlank(message = "Owner by is required")
    private UUID ownerBy; 
    
    private String mapUrl;
    
    private String lat;

    private String lng;


    

} // end class


