package com.cfloresh.springboot.app.personalfinance.service.categories;

import com.cfloresh.springboot.app.personalfinance.dto.category.CategoryResponseDto;
import com.cfloresh.springboot.app.personalfinance.exception.ResourceNotFoundException;
import com.cfloresh.springboot.app.personalfinance.mapper.CategoryMapper;
import com.cfloresh.springboot.app.personalfinance.model.categories.Category;
import com.cfloresh.springboot.app.personalfinance.repository.categories.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class CategoriesService {

    private final CategoryRepository repository;

    public CategoriesService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<CategoryResponseDto> getAllCategories() {
        log.debug("Fetching all categories");

        List<CategoryResponseDto> categories = StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(CategoryMapper::toResponseDto)
                .collect(Collectors.toList());

        log.info("Retrieved {} categories", categories.size());

        return categories;
    }

    public Category findById(Long id) {
        log.debug("Fetching category with id: {}", id);

        return repository.findById(id).orElseThrow(() -> {
            log.warn("Category with id: {} not found", id);
            return new ResourceNotFoundException("Category couldn't be found");
        });
    }
}
