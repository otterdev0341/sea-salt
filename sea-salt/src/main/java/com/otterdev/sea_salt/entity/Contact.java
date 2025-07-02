package com.otterdev.sea_salt.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact {

    @Id
    private UUID id;

    @Column("business_name")
    private String businessName;

    @Column("internal_name")
    private String internalName;

    @Column("detail")
    private String detail;

    @Column("note")
    private String note;

    @Column("contact_type_id")
    private UUID contactTypeId;

    @Column("address")
    private String address;

    @Column("phone")
    private String phone;

    @Column("mobile_phone")
    private String mobilePhone;

    @Column("line")
    private String line;

    @Column("email")
    private String email;

    @Column("created_by")
    private UUID createdBy;

    @Column("created_at")
    private LocalDateTime createdAt;
    
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
