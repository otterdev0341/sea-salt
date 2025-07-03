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
@Table("expense_file")
public class ExpenseFile {
    
    @Column("expense_id")
    private UUID expenseId;
    
    @Column("file_id")
    private UUID fileId;
}
