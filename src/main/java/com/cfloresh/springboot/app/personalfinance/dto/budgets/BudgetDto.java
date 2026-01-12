package com.cfloresh.springboot.app.personalfinance.dto.budgets;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BudgetDto(@NotNull Long categoryId, @NotNull BigDecimal budgetAmount,
                        @NotNull String theme) {
}
