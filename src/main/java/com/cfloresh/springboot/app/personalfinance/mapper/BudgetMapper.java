package com.cfloresh.springboot.app.personalfinance.mapper;

import com.cfloresh.springboot.app.personalfinance.dto.budgets.BudgetResponseDto;
import com.cfloresh.springboot.app.personalfinance.dto.category.CategoryResponseDto;
import com.cfloresh.springboot.app.personalfinance.model.budgets.Budget;

import java.math.BigDecimal;

public class BudgetMapper {

    public static BudgetResponseDto toResponseDto(Budget budgetData, BigDecimal totalSpent,
                                                  BigDecimal remaining) {
        CategoryResponseDto categoryDto =
                    CategoryMapper.toResponseDto(budgetData.getCategory());

        return new BudgetResponseDto(budgetData.getId(),
                categoryDto, budgetData.getBudgetAmount(), budgetData.getTheme(), totalSpent, remaining);
    }
}
