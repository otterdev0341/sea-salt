package com.otterdev.domain.valueObject.dto.memo;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqUpdateMemoDto {
    
    @NotBlank(message = "name of memo is required")
    private String name;

    private String detail;

    @NotBlank(message = "Memo type is required")
    private UUID memoType;

}
