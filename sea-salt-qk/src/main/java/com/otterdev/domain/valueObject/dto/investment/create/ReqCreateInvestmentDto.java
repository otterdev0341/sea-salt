package com.otterdev.domain.valueObject.dto.investment.create;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.fasterxml.jackson.annotation.JsonFormat;

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
public class ReqCreateInvestmentDto {
    

    @FormParam("paymentt-date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @NotBlank(message = "Sale date cannot be blank")
    private LocalDateTime date;


    @FormParam("note")
    @NotBlank(message = "Investment note cannot be blank")
    private String note;

    @FormParam("property")
    @NotNull(message = "Property cannot be null")
    private UUID property;

    @Valid
    @FormParam("transaction-item")
    @NotNull(message = "Investment items cannot be null")
    private List<ReqCreateInvestmentItemDto> transactionItems;
    
    @FormParam("files")
    private List<FileUpload> files;

}
