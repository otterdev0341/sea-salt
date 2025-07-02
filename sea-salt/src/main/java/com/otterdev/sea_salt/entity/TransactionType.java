package com.otterdev.sea_salt.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionType {

    private UUID id;

    private String detail;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}