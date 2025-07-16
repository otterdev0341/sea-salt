package com.otterdev.domain.valueObject.dto.investment.update;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.FormParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqUpdateInvestmentItemDto {
    
    @FormParam("contact")
    @NotBlank(message = "Contact name cannot be blank")
    private UUID contact;

    @FormParam("price")
    @Positive(message = "Price must be a positive number")
    @NotBlank(message = "Price cannot be blank")
    private Double price;

}
