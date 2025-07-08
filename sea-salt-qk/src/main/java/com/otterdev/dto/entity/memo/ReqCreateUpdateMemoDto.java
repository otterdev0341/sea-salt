package com.otterdev.dto.entity.memo;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreateUpdateMemoDto {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Memo type cannot be blank")
    private UUID memoType;
    
    @NotBlank(message = "Detail cannot be blank")
    private String detail;

    @NotBlank(message = "Created at cannot be blank")
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
