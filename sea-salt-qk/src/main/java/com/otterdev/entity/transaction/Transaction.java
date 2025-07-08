package com.otterdev.entity.transaction;

import java.time.LocalDateTime;
import java.util.UUID;

import com.otterdev.entity.table.Property;
import com.otterdev.entity.table.User;

import jakarta.persistence.Column;
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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name= "transaction")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name= "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "transaction_type_id", referencedColumnName = "id", nullable=false)
    private TransactionType transactionTyepId;

    @ManyToOne
    @JoinColumn(name = "property_id", referencedColumnName = "id", nullable = false)
    private Property propertyId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User userId;

    @Column(name= "created_at")
    private LocalDateTime createdAt;
}
