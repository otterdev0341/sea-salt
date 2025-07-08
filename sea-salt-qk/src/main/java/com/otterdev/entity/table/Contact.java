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
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "contact", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"email", "created_by"})
})
@Data // Lombok will generate getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "business_name", nullable = false, unique = true)
    private String businessName;

    @Column(name = "internal_name")
    private String internalName;

    @Column(name = "detail")
    private String detail;

    @Column(name = "note")
    private String note;

    // 💡 Relation to ContactType
    @ManyToOne
    @JoinColumn(name = "contact_type_id", referencedColumnName = "id", nullable = false)
    private ContactType contactType;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "mobile_phone")
    private String mobilePhone;

    @Column(name = "line")
    private String line;

    @Column(name = "email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false)
    private User createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
