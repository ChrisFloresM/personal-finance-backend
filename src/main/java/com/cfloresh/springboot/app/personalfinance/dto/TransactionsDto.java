package com.cfloresh.springboot.app.personalfinance.dto;


import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionsDto(@NotNull String avatar, @NotNull String name,
                              @NotNull String category, @NotNull LocalDate date,
                              @NotNull BigDecimal amount, @NotNull boolean recurring) {
}
