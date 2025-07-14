package com.otterdev.domain.entity;

import java.util.UUID;

import org.hibernate.validator.constraints.UniqueElements;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Table(name = "transaction_types")
@AllArgsConstructor
@NoArgsConstructor
public class TransactionType {
    
    @Id
    private UUID id;

    @Column(name = "detail", unique = true, nullable = false)
    @UniqueElements
    private String detail;

}
