package com.cfloresh.springboot.app.personalfinance.dto.transactions;

import com.cfloresh.springboot.app.personalfinance.dto.category.CategoryResponseDto;
import com.cfloresh.springboot.app.personalfinance.model.categories.Category;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponseDto(@NotNull Long transactionId, @NotNull String avatar,
                                     @NotNull String name,
                                     @NotNull CategoryResponseDto category, @NotNull LocalDate date,
                                     @NotNull BigDecimal amount, @NotNull boolean recurring) {
}
