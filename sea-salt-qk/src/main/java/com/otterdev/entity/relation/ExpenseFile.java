package com.otterdev.entity.relation;


import java.util.UUID;

import com.otterdev.entity.table.Expense;
import com.otterdev.entity.table.FileDetail;

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
@Table(name= "expense_file")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseFile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expenseId;
    
    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    private FileDetail fileId;
}
