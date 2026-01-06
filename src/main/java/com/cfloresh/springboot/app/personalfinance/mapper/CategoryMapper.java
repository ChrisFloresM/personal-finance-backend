package com.cfloresh.springboot.app.personalfinance.mapper;

import com.cfloresh.springboot.app.personalfinance.dto.category.CategoryResponseDto;
import com.cfloresh.springboot.app.personalfinance.model.categories.Category;

public class CategoryMapper {
    public static CategoryResponseDto toResponseDto(Category category) {
        return new CategoryResponseDto(category.getId(), category.getKey(), category.getLabel());
    }
}
