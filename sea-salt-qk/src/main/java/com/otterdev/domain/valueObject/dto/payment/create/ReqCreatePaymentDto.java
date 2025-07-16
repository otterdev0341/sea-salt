package com.otterdev.domain.valueObject.dto.payment.create;

import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.FormParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreatePaymentDto {
    
    @FormParam("note")
    @NotBlank
    private String note;
    
    @FormParam("property")
    @NotNull(message = "Property cannot be null")
    private UUID property;

    @FormParam("contact")
    @NotNull(message = "Contact cannot be null")
    private UUID contact;

    @FormParam("payment-items")
    @NotNull(message = "Payment Items cannot be null")
    @Valid
    private List<ReqCreatePaymentItemDto> paymentItems;

    @FormParam("files")
    private List<FileUpload> files;
}
