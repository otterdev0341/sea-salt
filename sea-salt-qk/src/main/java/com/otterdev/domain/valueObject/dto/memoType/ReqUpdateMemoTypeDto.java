package com.otterdev.domain.valueObject.dto.memoType;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqUpdateMemoTypeDto {
    @NotBlank(message = "Memo type detail is required")
    private String detail;
}
