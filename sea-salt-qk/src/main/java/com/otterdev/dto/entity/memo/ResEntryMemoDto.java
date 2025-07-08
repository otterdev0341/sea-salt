package com.otterdev.dto.entity.memo;

import java.time.LocalDateTime;
import java.util.UUID;

import com.otterdev.dto.entity.memoType.ResEntryMemoTypeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResEntryMemoDto {
    
    private UUID id;

    private String name;

    private ResEntryMemoTypeDto memoType;    
    
    private String detail;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
