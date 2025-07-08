package com.otterdev.entity.table;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "property", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "owner_by", "created_by"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name= "name")
    private String name;

    @Column(name= "description")
    private String description;

    @Column(name= "specific")
    private String specific;

    @Column(name= "hilight")
    private String hilight;

    @Column(name= "area")
    private String area;

    @Column(name= "price")
    private Double price;

    @Column(name= "f_s_p")
    private Double fsp; // mapped from `f_s_p` (adjusted field name)

    @ManyToOne
    @JoinColumn(name = "status", referencedColumnName = "id", nullable = false)
    private PropertyStatus status;

    @ManyToOne
    @JoinColumn(name = "owner_by", referencedColumnName = "id", nullable = false)
    private User ownerBy;

    @Column(name= "map_usl")
    private String mapUrl;

    @Column(name= "lat")
    private String lat;

    @Column(name= "long")
    private String lng; // mapped from `long` to avoid Java keyword conflict

    @Column(name= "sold")
    private Boolean sold;

    @Column(name= "created_by")
    private UUID createdBy;

    @Column(name= "created_at")
    private LocalDateTime createdAt;

    @Column(name= "updated_at")
    private LocalDateTime updatedAt;
}
