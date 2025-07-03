package com.otterdev.sea_salt.entity.join;

import java.util.UUID;

import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyPropertyType {
    
    @Column("property_id")
    private UUID propertyId;

    @Column("property_type_id")
    private UUID PropertyTypeId;
}
