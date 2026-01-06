package com.cfloresh.springboot.app.personalfinance.controller.categories;

import com.cfloresh.springboot.app.personalfinance.dto.category.CategoryResponseDto;
import com.cfloresh.springboot.app.personalfinance.model.categories.Category;
import com.cfloresh.springboot.app.personalfinance.service.categories.CategoriesService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoriesController {

    private final CategoriesService service;

    public CategoriesController(CategoriesService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoryResponseDto> getAllCategories() {
        return service.getAllCategories();
    }
}
