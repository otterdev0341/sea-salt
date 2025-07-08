package com.otterdev.entity.transaction;

import java.time.LocalDateTime;
import java.util.UUID;

import com.otterdev.entity.table.Contact;
import com.otterdev.entity.table.Property;

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
@Table(name= "invest_transaction_item")
public class InvestTransactionItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "invest_transaction_id", referencedColumnName = "id", nullable=false)
    private InvestTransaction investTransactionId;

    @ManyToOne
    @JoinColumn(name = "property_id", referencedColumnName = "id", nullable = false)
    private Property propertyId;

    @ManyToOne
    @JoinColumn(name = "contact_id", referencedColumnName = "id", nullable =false)
    private Contact contactId;

    @Column(name= "amount")
    private Double amount;

    @Column(name= "funding_percent")
    private Double fundingPercent;

    @Column(name= "created_at")
    private LocalDateTime createdAt;

    @Column(name= "updated_at")
    private LocalDateTime updatedAt;

}
