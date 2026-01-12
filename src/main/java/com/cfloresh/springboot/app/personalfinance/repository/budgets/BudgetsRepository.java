package com.cfloresh.springboot.app.personalfinance.repository.budgets;

import com.cfloresh.springboot.app.personalfinance.model.budgets.Budget;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BudgetsRepository extends CrudRepository<Budget, Long> {
    public List<Budget> findAllByUserId(Long userId);
}
