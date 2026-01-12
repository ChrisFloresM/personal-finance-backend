package com.cfloresh.springboot.app.personalfinance.dto.budgets;

import com.cfloresh.springboot.app.personalfinance.dto.category.CategoryResponseDto;
import com.cfloresh.springboot.app.personalfinance.model.categories.Category;

import java.math.BigDecimal;

public record BudgetResponseDto(Long id, CategoryResponseDto category, BigDecimal budgetAmount,
                                String theme, BigDecimal totalSpent, BigDecimal remaining) {
}
