package com.otterdev.entity.relation;


import java.util.UUID;

import com.otterdev.entity.table.Property;
import com.otterdev.entity.table.PropertyType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "property_property_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyPropertyType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "property_id", referencedColumnName = "id", nullable = false)
    private Property property;

    @ManyToOne
    @JoinColumn(name = "property_type_id", referencedColumnName = "id", nullable = false)
    private PropertyType propertyType;
}
