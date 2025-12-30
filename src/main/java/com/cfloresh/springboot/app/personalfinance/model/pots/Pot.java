package com.cfloresh.springboot.app.personalfinance.model.pots;

import com.cfloresh.springboot.app.personalfinance.model.users.AppUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "pots")
@Data
@NoArgsConstructor
public class Pot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    private String name;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal target;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal total;

    private String theme;
}
