package com.cfloresh.springboot.app.personalfinance.model.transactions;

import com.cfloresh.springboot.app.personalfinance.model.categories.Category;
import com.cfloresh.springboot.app.personalfinance.model.users.AppUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private String avatar;
    private String name;
    private LocalDate date;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;

    private boolean recurring;
}
