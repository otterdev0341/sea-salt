package com.otterdev.dto.entity.contact;

import java.time.LocalDateTime;
import java.util.UUID;

import com.otterdev.dto.entity.contactType.ResEntryContactTypeDto;

import lombok.Data;

@Data
public class ResEntryContactDto {
    
    private UUID id;

    private String businessName;

    private String internalName;

    private String detail;

    private String note;

    private ResEntryContactTypeDto contactTypeId;

    private String address;

    private String phone;

    private String mobilePhone;

    private String line;

    private String email;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
