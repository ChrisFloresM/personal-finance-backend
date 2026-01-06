package com.cfloresh.springboot.app.personalfinance.repository.categories;

import com.cfloresh.springboot.app.personalfinance.model.categories.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
