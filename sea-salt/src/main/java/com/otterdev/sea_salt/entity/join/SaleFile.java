package com.otterdev.sea_salt.entity.join;

import java.util.UUID;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("sale_file")
public class SaleFile {
    
    @Column("sale_id")
    private UUID saleId;

    @Column("file_id")
    private UUID fileId;
}
