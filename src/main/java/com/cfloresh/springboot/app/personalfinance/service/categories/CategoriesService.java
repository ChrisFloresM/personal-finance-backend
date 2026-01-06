package com.cfloresh.springboot.app.personalfinance.service.categories;

import com.cfloresh.springboot.app.personalfinance.dto.category.CategoryResponseDto;
import com.cfloresh.springboot.app.personalfinance.exception.ResourceNotFoundException;
import com.cfloresh.springboot.app.personalfinance.mapper.CategoryMapper;
import com.cfloresh.springboot.app.personalfinance.model.categories.Category;
import com.cfloresh.springboot.app.personalfinance.repository.categories.CategoryRepository;
import com.cfloresh.springboot.app.personalfinance.service.users.UsersService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoriesService {

    private final CategoryRepository repository;

    public CategoriesService(CategoryRepository repository, UsersService usersService) {
        this.repository = repository;
    }

    public List<CategoryResponseDto> getAllCategories() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).map(CategoryMapper
        ::toResponseDto).collect(Collectors.toList());
    }

    public Category findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category " +
                "couldn't be found"));
    }
}
