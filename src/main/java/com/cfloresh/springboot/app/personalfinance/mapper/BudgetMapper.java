package com.cfloresh.springboot.app.personalfinance.mapper;

import com.cfloresh.springboot.app.personalfinance.dto.budgets.BudgetResponseDto;
import com.cfloresh.springboot.app.personalfinance.dto.budgets.BudgetResponseWListDto;
import com.cfloresh.springboot.app.personalfinance.dto.category.CategoryResponseDto;
import com.cfloresh.springboot.app.personalfinance.dto.transactions.TransactionResponseDto;
import com.cfloresh.springboot.app.personalfinance.model.budgets.Budget;

import java.math.BigDecimal;
import java.util.List;

public class BudgetMapper {

    public static BudgetResponseDto toResponseDto(Budget budgetData, BigDecimal totalSpent,
                                                  BigDecimal remaining) {
        CategoryResponseDto categoryDto =
                    CategoryMapper.toResponseDto(budgetData.getCategory());

        return new BudgetResponseDto(budgetData.getId(),
                categoryDto, budgetData.getBudgetAmount(), budgetData.getTheme(), totalSpent, remaining);
    }

    public static BudgetResponseWListDto toResponseWListDto(Budget budgetData,
                                                            BigDecimal totalSpent,
                                                            BigDecimal remaining,
                                                            List<TransactionResponseDto> budgetTransactions) {
        CategoryResponseDto categoryDto =
                    CategoryMapper.toResponseDto(budgetData.getCategory());

        return new BudgetResponseWListDto(budgetData.getId(),
                categoryDto, budgetData.getBudgetAmount(), budgetData.getTheme(), totalSpent,
                remaining, budgetTransactions);
    }
}
