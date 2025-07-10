package com.otterdev.dto.entity.propertyStatus;

import java.util.UUID;

import com.otterdev.entity.table.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResEntryPropertyStatusDto {
    
    private UUID id;
    private String detail;
    private User createdBy;
    private String createdAt;
    private String updatedAt;

    
}
