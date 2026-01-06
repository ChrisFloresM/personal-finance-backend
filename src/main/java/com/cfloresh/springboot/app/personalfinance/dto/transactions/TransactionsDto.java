package com.cfloresh.springboot.app.personalfinance.dto.transactions;


import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionsDto(@NotNull String avatar, @NotNull String name,
                              @NotNull Long categoryId, @NotNull LocalDate date,
                              @NotNull BigDecimal amount, @NotNull boolean recurring) {
}
