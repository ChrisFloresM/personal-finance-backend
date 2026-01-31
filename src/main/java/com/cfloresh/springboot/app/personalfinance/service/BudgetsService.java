package com.cfloresh.springboot.app.personalfinance.service;

import com.cfloresh.springboot.app.personalfinance.dto.budgets.BudgetDto;
import com.cfloresh.springboot.app.personalfinance.dto.budgets.BudgetResponseDto;
import com.cfloresh.springboot.app.personalfinance.dto.budgets.BudgetResponseWListDto;
import com.cfloresh.springboot.app.personalfinance.dto.transactions.TransactionResponseDto;
import com.cfloresh.springboot.app.personalfinance.exception.DuplicateBudgetException;
import com.cfloresh.springboot.app.personalfinance.mapper.BudgetMapper;
import com.cfloresh.springboot.app.personalfinance.model.budgets.Budget;
import com.cfloresh.springboot.app.personalfinance.model.categories.Category;
import com.cfloresh.springboot.app.personalfinance.model.transactions.Transaction;
import com.cfloresh.springboot.app.personalfinance.model.users.AppUser;
import com.cfloresh.springboot.app.personalfinance.repository.budgets.BudgetsRepository;
import com.cfloresh.springboot.app.personalfinance.service.categories.CategoriesService;
import com.cfloresh.springboot.app.personalfinance.service.transactions.TransactionsService;
import com.cfloresh.springboot.app.personalfinance.service.users.UsersService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BudgetsService {

    private final BudgetsRepository repository;
    private final UsersService usersService;
    private final CategoriesService categoriesService;
    private final TransactionsService transactionsService;

    public BudgetsService(BudgetsRepository repository, UsersService usersService, CategoriesService categoriesService, TransactionsService transactionsService) {
        this.repository = repository;
        this.usersService = usersService;
        this.categoriesService = categoriesService;
        this.transactionsService = transactionsService;
    }

    public BudgetResponseDto createBudget(Jwt jwt, BudgetDto budgetData) {
        AppUser user = usersService.findUser(jwt.getClaim("sub"));
        Category category = categoriesService.findById(budgetData.categoryId());

        Budget newBudget = new Budget();
        newBudget.setUser(user);
        newBudget.setCategory(category);
        newBudget.setBudgetAmount(budgetData.budgetAmount());
        newBudget.setTheme(budgetData.theme());

            Budget savedBudget;

        try {
            savedBudget = repository.save(newBudget);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateBudgetException("A budget for this category already exists");
        }

        BigDecimal spent = calculateBudgetSpent(savedBudget);
        BigDecimal remaining = savedBudget.getBudgetAmount().add(spent);

        return BudgetMapper.toResponseDto(savedBudget, spent, remaining);
    }

    public List<BudgetResponseDto> getBudgets(Jwt jwt) {
        AppUser user = usersService.findUser(jwt.getClaim("sub"));

        List<Budget> budgets = repository.findAllByUserId(user.getId());

        return budgets.stream().map((budget) -> {
            BigDecimal spent = calculateBudgetSpent(budget);
            BigDecimal remaining = budget.getBudgetAmount().add(spent);

            return BudgetMapper.toResponseDto(budget, spent, remaining);
        }).toList();
    }

    public List<BudgetResponseWListDto> getBudgetsWList(Jwt jwt) {
        AppUser user = usersService.findUser(jwt.getClaim("sub"));

        List<Budget> budgets = repository.findAllByUserId(user.getId());
        List<TransactionResponseDto> transactions = transactionsService.getBudgetTransactions(jwt);

        Map<Long, List<TransactionResponseDto>> transactionsByCategory =
                transactions.stream().collect(Collectors.groupingBy(transaction -> transaction.category().id()));

        return budgets.stream().map(budget -> {
            List<TransactionResponseDto> budgetTransactions = transactionsByCategory
                    .getOrDefault(budget.getCategory().getId(), List.of())
                    .stream()
                    .limit(3)
                    .toList();

            BigDecimal spent = calculateBudgetSpent(budget);
            BigDecimal remaining = budget.getBudgetAmount().add(spent);

            return BudgetMapper.toResponseWListDto(budget, spent, remaining, budgetTransactions);

        }).toList();
    }

    private BigDecimal calculateBudgetSpent(Budget budget) {
        return transactionsService.getTotalSpentByCategory(budget.getUser().getId(),
                        budget.getCategory().getId());
    }
}


