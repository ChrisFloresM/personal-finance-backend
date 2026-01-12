package com.cfloresh.springboot.app.personalfinance.model.categories;

import com.cfloresh.springboot.app.personalfinance.model.budgets.Budget;
import com.cfloresh.springboot.app.personalfinance.model.transactions.Transaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="categories")
@NoArgsConstructor
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    String key;

    @NotNull
    String label;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    List<Budget> budgets = new ArrayList<>();
}
