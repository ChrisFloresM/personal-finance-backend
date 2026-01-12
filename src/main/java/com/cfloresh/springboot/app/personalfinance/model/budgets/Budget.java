package com.cfloresh.springboot.app.personalfinance.model.budgets;

import com.cfloresh.springboot.app.personalfinance.model.categories.Category;
import com.cfloresh.springboot.app.personalfinance.model.users.AppUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name="budgets",
        uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "category_id"})})
public class Budget {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @NotNull
    @Column(name="budget_amount")
    private BigDecimal budgetAmount;

    @NotNull
    private String theme;
}
