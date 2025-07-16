package com.otterdev.domain.valueObject.sale;

import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.FormParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreateSaleDto {

    @FormParam("property")
    @NotBlank(message = "Property ID cannot be blank")
    private UUID property;
    
    @FormParam("contact")
    @NotBlank(message = "Contact ID cannot be blank")
    private UUID contact;

    @FormParam("note")
    @NotBlank(message = "Sale note cannot be blank")
    private String note;

    @FormParam("price")
    @NotBlank(message = "Sale price cannot be blank")
    @Min(value = 0, message = "Sale price must be at least 0")
    private Double price;

    @FormParam("files")
    private List<FileUpload> files;

}
