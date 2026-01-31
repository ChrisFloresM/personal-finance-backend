package com.cfloresh.springboot.app.personalfinance.dto.budgets;

import com.cfloresh.springboot.app.personalfinance.dto.category.CategoryResponseDto;
import com.cfloresh.springboot.app.personalfinance.dto.transactions.TransactionResponseDto;
import com.cfloresh.springboot.app.personalfinance.model.transactions.Transaction;

import java.math.BigDecimal;
import java.util.List;

public record BudgetResponseWListDto(Long id, CategoryResponseDto category, BigDecimal budgetAmount,
                                     String theme, BigDecimal totalSpent, BigDecimal remaining,
                                     List<TransactionResponseDto> latestTransactions) {
}
