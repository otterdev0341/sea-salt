package com.otterdev.dto.entity.memoType;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResEntryMemoTypeDto {
    private UUID id;
    private String detail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
