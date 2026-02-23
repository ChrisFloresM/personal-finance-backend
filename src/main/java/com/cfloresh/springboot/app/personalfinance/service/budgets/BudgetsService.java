package com.cfloresh.springboot.app.personalfinance.service.budgets;

import com.cfloresh.springboot.app.personalfinance.dto.budgets.BudgetDto;
import com.cfloresh.springboot.app.personalfinance.dto.budgets.BudgetResponseDto;
import com.cfloresh.springboot.app.personalfinance.dto.budgets.BudgetResponseWListDto;
import com.cfloresh.springboot.app.personalfinance.dto.transactions.TransactionResponseDto;
import com.cfloresh.springboot.app.personalfinance.exception.DuplicateBudgetException;
import com.cfloresh.springboot.app.personalfinance.exception.ForbiddenException;
import com.cfloresh.springboot.app.personalfinance.exception.ResourceNotFoundException;
import com.cfloresh.springboot.app.personalfinance.mapper.BudgetMapper;
import com.cfloresh.springboot.app.personalfinance.model.budgets.Budget;
import com.cfloresh.springboot.app.personalfinance.model.categories.Category;
import com.cfloresh.springboot.app.personalfinance.model.users.AppUser;
import com.cfloresh.springboot.app.personalfinance.repository.budgets.BudgetsRepository;
import com.cfloresh.springboot.app.personalfinance.service.categories.CategoriesService;
import com.cfloresh.springboot.app.personalfinance.service.transactions.TransactionsService;
import com.cfloresh.springboot.app.personalfinance.service.users.UsersService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
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
        String userSub = jwt.getClaim("sub");
        log.debug("Creating budget for user: {} - Category: {}, Amount: {}",
                userSub, budgetData.categoryId(), budgetData.budgetAmount());

        AppUser user = usersService.findUser(userSub);
        Category category = categoriesService.findById(budgetData.categoryId());

        Budget newBudget = new Budget();
        newBudget.setUser(user);

        setBudgetData(newBudget, budgetData, category);

        Budget savedBudget = saveBudget(newBudget);

        log.info("Budget created successfully for user with id: {} - Category: {}, Amount: {}",
                user.getId(), category.getLabel(), budgetData.budgetAmount());

        return toResponse(savedBudget);
    }


    public BudgetResponseDto editBudget(Jwt jwt, Long budgetId, BudgetDto budgetData) {
        String userSub = jwt.getClaim("sub");
        log.debug("Editing budget with id: {} for user: {} - Category: {}, New amount: {}",
                budgetId, userSub, budgetData.categoryId(), budgetData.budgetAmount());

        AppUser user = usersService.findUser(userSub);
        Category category = categoriesService.findById(budgetData.categoryId());

        Budget budget =
                repository.findByIdAndUser_Id(budgetId, user.getId()).orElseThrow(() -> {
                    log.warn("Budget with id: {} not found for user with id: {}", budgetId, user.getId());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Budget not found");
                });

        setBudgetData(budget, budgetData, category);

        Budget savedBudget = saveBudget(budget);

        log.info("Budget with id: {} edited successfully for user with id: {} - New amount: {}",
                budgetId, user.getId(), budgetData.budgetAmount());

        return toResponse(savedBudget);
    }

    private Budget saveBudget(Budget budget) {
        try {
            Budget saved = repository.save(budget);
            log.debug("Budget saved successfully with id: {}", saved.getId());
            return saved;
        } catch (DataIntegrityViolationException ex) {
            log.warn("Attempt to create duplicate budget for category id: {}", budget.getCategory().getId());
            throw new DuplicateBudgetException("A budget for this category already exists");
        }
    }

    private BudgetResponseDto toResponse(Budget budget) {
        BigDecimal spent = calculateBudgetSpent(budget);
        BigDecimal remaining = budget.getBudgetAmount().add(spent);

        return BudgetMapper.toResponseDto(budget, spent, remaining);
    }

    private void setBudgetData(Budget budget, BudgetDto budgetData, Category category) {
        budget.setCategory(category);
        budget.setBudgetAmount(budgetData.budgetAmount());
        budget.setTheme(budgetData.theme());
    }

    public List<BudgetResponseDto> getBudgets(Jwt jwt) {
        String userSub = jwt.getClaim("sub");
        log.debug("Fetching all budgets for user: {}", userSub);

        AppUser user = usersService.findUser(userSub);

        List<Budget> budgets = repository.findAllByUserIdOrderByIdAsc(user.getId());

        log.info("Retrieved {} budgets for user with id: {}", budgets.size(), user.getId());

        return budgets.stream().map((budget) -> {
            BigDecimal spent = calculateBudgetSpent(budget);
            BigDecimal remaining = budget.getBudgetAmount().add(spent);

            return BudgetMapper.toResponseDto(budget, spent, remaining);
        }).toList();
    }

    public List<BudgetResponseWListDto> getBudgetsWList(Jwt jwt) {
        String userSub = jwt.getClaim("sub");
        log.debug("Fetching budgets with transaction list for user: {}", userSub);

        AppUser user = usersService.findUser(userSub);

        List<Budget> budgets = repository.findAllByUserIdOrderByIdAsc(user.getId());
        List<TransactionResponseDto> transactions = transactionsService.getBudgetTransactions(jwt);

        Map<Long, List<TransactionResponseDto>> transactionsByCategory =
                transactions.stream().collect(Collectors.groupingBy(transaction -> transaction.category().id()));

        log.info("Retrieved {} budgets with {} total transactions for user with id: {}",
                budgets.size(), transactions.size(), user.getId());

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

    @Transactional
    public void deleteBudget(Jwt jwt, Long budgetId) {
        String userSub = jwt.getClaim("sub");
        log.debug("Deleting budget with id: {} for user: {}", budgetId, userSub);

        AppUser user = usersService.findUser(userSub);

        Budget budget =
                repository.findById(budgetId).orElseThrow(() -> new ResourceNotFoundException(
                        "Resource not " +
                        "found. Id: " + budgetId));

        if (!budget.getUser().getId().equals(user.getId())) {
            log.warn("User with id: {} attempted to delete budget with id: {} which belongs to user with id: {}",
                    user.getId(), budgetId, budget.getUser().getId());
            throw new ForbiddenException("User has not access to this resource");
        }

        repository.deleteByIdAndUser_Id(budgetId, user.getId());

        log.info("Budget with id: {} deleted successfully for user with id: {}", budgetId, user.getId());
    }
}


