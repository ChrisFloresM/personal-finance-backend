package com.cfloresh.springboot.app.personalfinance.service.budgets;

import com.cfloresh.springboot.app.personalfinance.dto.budgets.BudgetDto;
import com.cfloresh.springboot.app.personalfinance.dto.budgets.BudgetResponseDto;
import com.cfloresh.springboot.app.personalfinance.dto.budgets.BudgetResponseWListDto;
import com.cfloresh.springboot.app.personalfinance.exception.DuplicateBudgetException;
import com.cfloresh.springboot.app.personalfinance.exception.ForbiddenException;
import com.cfloresh.springboot.app.personalfinance.exception.ResourceNotFoundException;
import com.cfloresh.springboot.app.personalfinance.model.budgets.Budget;
import com.cfloresh.springboot.app.personalfinance.model.categories.Category;
import com.cfloresh.springboot.app.personalfinance.model.users.AppUser;
import com.cfloresh.springboot.app.personalfinance.repository.budgets.BudgetsRepository;
import com.cfloresh.springboot.app.personalfinance.service.categories.CategoriesService;
import com.cfloresh.springboot.app.personalfinance.service.transactions.TransactionsService;
import com.cfloresh.springboot.app.personalfinance.service.users.UsersService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BudgetsServiceTest {

    @Mock
    private BudgetsRepository repository;

    @Mock
    private UsersService usersService;

    @Mock
    private CategoriesService categoriesService;

    @Mock
    private TransactionsService transactionsService;

    @InjectMocks
    private BudgetsService budgetsService;

    private Jwt mockJwt() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaim("sub")).thenReturn("user123");
        return jwt;
    }

    private AppUser mockUser() {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setAuthId("user123");
        return user;
    }

    private Category mockCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setKey("entertainment");
        category.setLabel("Entertainment");
        return category;
    }

    private Budget mockBudget() {
        Budget budget = new Budget();
        budget.setId(1L);
        budget.setUser(mockUser());
        budget.setCategory(mockCategory());
        budget.setBudgetAmount(new BigDecimal("500"));
        budget.setTheme("#FFFFFF");
        return budget;
    }

    @Test
    @DisplayName("Create a budget successfully")
    void createBudgetSuccessTest() {
        Jwt jwt = mockJwt();
        AppUser user = mockUser();
        Category category = mockCategory();

        BudgetDto inputData = new BudgetDto(1L, new BigDecimal("500"), "#FFFFFF");

        when(usersService.findUser("user123")).thenReturn(user);
        when(categoriesService.findById(1L)).thenReturn(category);
        when(transactionsService.getTotalSpentByCategory(1L, 1L)).thenReturn(new BigDecimal("500"));

        Budget savedBudget = new Budget();
        savedBudget.setId(1L);
        savedBudget.setUser(user);
        savedBudget.setCategory(category);
        savedBudget.setBudgetAmount(new BigDecimal("500"));
        savedBudget.setTheme("#FFFFFF");

        when(repository.save(any(Budget.class))).thenReturn(savedBudget);

        BudgetResponseDto result = budgetsService. createBudget(jwt, inputData);

        assertNotNull(result);
        assertEquals(new BigDecimal("500"), result.budgetAmount());

        verify(usersService, times(1)).findUser(any(String.class));
        verify(categoriesService, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Budget.class));
    }

    @Test
    @DisplayName("Throw DuplicateBudgetException when creating budget with same category")
    void createBudgetDuplicateTest() {
        Jwt jwt = mockJwt();
        AppUser user = mockUser();
        Category category = mockCategory();

        BudgetDto inputData = new BudgetDto(1L, new BigDecimal("500"), "#FFFFFF");

        when(usersService.findUser("user123")).thenReturn(user);
        when(categoriesService.findById(1L)).thenReturn(category);
        when(repository.save(any(Budget.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate key value"));

        assertThrows(DuplicateBudgetException.class,
                () -> budgetsService.createBudget(jwt, inputData));

        verify(repository, times(1)).save(any(Budget.class));
    }

    @Test
    @DisplayName("Get all budgets for user")
    void getBudgetsTest() {
        Jwt jwt = mockJwt();
        AppUser user = mockUser();

        when(usersService.findUser("user123")).thenReturn(user);
        when(transactionsService.getTotalSpentByCategory(1L, 1L)).thenReturn(new BigDecimal("500"));

        Budget budget = mockBudget();
        when(repository.findAllByUserIdOrderByIdAsc(1L))
                .thenReturn(List.of(budget));

        List<BudgetResponseDto> result = budgetsService.getBudgets(jwt);

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("500"), result.getFirst().budgetAmount());

        verify(repository, times(1)).findAllByUserIdOrderByIdAsc(1L);
    }

    @Test
    @DisplayName("Get budgets with transaction list")
    void getBudgetsWListTest() {
        Jwt jwt = mockJwt();
        AppUser user = mockUser();

        when(usersService.findUser("user123")).thenReturn(user);

        Budget budget = mockBudget();
        when(repository.findAllByUserIdOrderByIdAsc(1L))
                .thenReturn(List.of(budget));
        when(transactionsService.getBudgetTransactions(jwt))
                .thenReturn(List.of());
        when(transactionsService.getTotalSpentByCategory(1L, 1L)).thenReturn(new BigDecimal("500"));

        List<BudgetResponseWListDto> result = budgetsService.getBudgetsWList(jwt);

        assertEquals(1, result.size());

        verify(repository, times(1)).findAllByUserIdOrderByIdAsc(1L);
        verify(transactionsService, times(1)).getBudgetTransactions(jwt);
    }

    @Test
    @DisplayName("Edit budget successfully")
    void editBudgetSuccessTest() {
        Jwt jwt = mockJwt();
        AppUser user = mockUser();
        Category category = mockCategory();

        BudgetDto updateData = new BudgetDto(1L, new BigDecimal("750"), "#FFFFFF");

        when(usersService.findUser("user123")).thenReturn(user);
        when(categoriesService.findById(1L)).thenReturn(category);

        Budget budget = mockBudget();
        when(repository.findByIdAndUser_Id(1L, 1L))
                .thenReturn(Optional.of(budget));
        when(repository.save(any(Budget.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionsService.getTotalSpentByCategory(1L, 1L)).thenReturn(new BigDecimal("500"));

        BudgetResponseDto result = budgetsService.editBudget(jwt, 1L, updateData);

        assertNotNull(result);
        assertEquals(new BigDecimal("750"), result.budgetAmount());

        verify(repository, times(1)).findByIdAndUser_Id(1L, 1L);
        verify(repository, times(1)).save(any(Budget.class));
    }

    @Test
    @DisplayName("Throw exception when budget not found for edit")
    void editBudgetNotFoundTest() {
        Jwt jwt = mockJwt();
        AppUser user = mockUser();
        Category category = mockCategory();

        BudgetDto updateData = new BudgetDto(1L, new BigDecimal("750"), "#FFFFFF");

        when(usersService.findUser("user123")).thenReturn(user);
        when(categoriesService.findById(1L)).thenReturn(category);
        when(repository.findByIdAndUser_Id(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> budgetsService.editBudget(jwt, 1L, updateData));

        verify(repository, times(1)).findByIdAndUser_Id(1L, 1L);
        verify(repository, never()).save(any(Budget.class));
    }

    @Test
    @DisplayName("Delete budget successfully")
    void deleteBudgetSuccessTest() {
        Jwt jwt = mockJwt();
        AppUser user = mockUser();

        Budget budget = mockBudget();
        when(usersService.findUser("user123")).thenReturn(user);
        when(repository.findById(1L))
                .thenReturn(Optional.of(budget));

        budgetsService.deleteBudget(jwt, 1L);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).deleteByIdAndUser_Id(1L, 1L);
    }

    @Test
    @DisplayName("Throw exception when deleting non-existent budget")
    void deleteBudgetNotFoundTest() {
        Jwt jwt = mockJwt();
        AppUser user = mockUser();

        when(usersService.findUser("user123")).thenReturn(user);
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> budgetsService.deleteBudget(jwt, 1L));

        verify(repository, times(1)).findById(1L);
        verify(repository, never()).deleteByIdAndUser_Id(any(), any());
    }

    @Test
    @DisplayName("Throw ForbiddenException when user tries to delete another user's budget")
    void deleteBudgetForbiddenTest() {
        Jwt jwt = mockJwt();
        AppUser user = mockUser();
        user.setId(999L);

        Budget budget = mockBudget();

        when(usersService.findUser("user123")).thenReturn(user);
        when(repository.findById(1L))
                .thenReturn(Optional.of(budget));

        assertThrows(ForbiddenException.class,
                () -> budgetsService.deleteBudget(jwt, 1L));

        verify(repository, times(1)).findById(1L);
        verify(repository, never()).deleteByIdAndUser_Id(any(), any());
    }

}


