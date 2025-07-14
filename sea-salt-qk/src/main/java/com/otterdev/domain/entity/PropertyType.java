package com.otterdev.domain.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.otterdev.domain.entity.relation.PropertyFileDetail;
import com.otterdev.domain.entity.relation.PropertyPropertyType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "property_types",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_property_types_detail_created_by",
            columnNames = {"detail", "created_by"}
        )
    },
    indexes = {
        @Index(
            name = "idx_property_types_created_by",
            columnList = "created_by"
        )
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "detail", nullable = false, length = 50)
    private String detail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "created_by",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(
            name = "fk_property_types_created_by",
            foreignKeyDefinition = 
                "FOREIGN KEY (created_by) REFERENCES users(id) " +
                "ON DELETE RESTRICT ON UPDATE CASCADE"
        )
    )
    private User createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // relation
    @OneToMany(mappedBy = "propertyType")
    private Set<PropertyPropertyType> properties = new HashSet<>();

    @OneToMany(mappedBy = "property")
    private Set<PropertyFileDetail> fileDetails = new HashSet<>();
}