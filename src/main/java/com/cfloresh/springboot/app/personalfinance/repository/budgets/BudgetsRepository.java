package com.cfloresh.springboot.app.personalfinance.repository.budgets;

import com.cfloresh.springboot.app.personalfinance.model.budgets.Budget;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetsRepository extends CrudRepository<Budget, Long> {
    List<Budget> findAllByUserIdOrderByIdAsc(Long userId);
    Optional<Budget> findByIdAndUser_Id(Long budgetId, Long userId);

    void deleteByIdAndUser_Id(Long budgetId, Long userId);

}
