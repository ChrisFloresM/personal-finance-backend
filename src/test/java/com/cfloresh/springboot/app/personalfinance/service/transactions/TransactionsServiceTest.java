package com.cfloresh.springboot.app.personalfinance.service.transactions;

import com.cfloresh.springboot.app.personalfinance.dto.transactions.TransactionPageResponseDto;
import com.cfloresh.springboot.app.personalfinance.dto.transactions.TransactionResponseDto;
import com.cfloresh.springboot.app.personalfinance.dto.transactions.TransactionsDto;
import com.cfloresh.springboot.app.personalfinance.model.categories.Category;
import com.cfloresh.springboot.app.personalfinance.model.transactions.Transaction;
import com.cfloresh.springboot.app.personalfinance.model.transactions.TransactionSort;
import com.cfloresh.springboot.app.personalfinance.model.users.AppUser;
import com.cfloresh.springboot.app.personalfinance.repository.transactions.TransactionsRespository;
import com.cfloresh.springboot.app.personalfinance.service.categories.CategoriesService;
import com.cfloresh.springboot.app.personalfinance.service.users.UsersService;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionsServiceTest {

    @Mock
    private TransactionsRespository repository;

    @Mock
    private UsersService usersService;

    @Mock
    private CategoriesService categoriesService;

    @InjectMocks
    private TransactionsService transactionsService;

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
        category.setKey("general");
        category.setLabel("General");
        return category;
    }

    @Test
    @DisplayName("Save a transaction to DB and return DTO")
    void saveTransactionTest() {

        Jwt jwt = mockJwt();
        AppUser user = mockUser();
        Category category = mockCategory();

        LocalDate date = LocalDate.now();

        TransactionsDto inputData = new TransactionsDto("/test/path", "Uber", 1L, date,
                new BigDecimal(1000), false);

        when(jwt.getClaim("sub")).thenReturn("user123");

        when(usersService.findUser("user123")).thenReturn(user);
        when(categoriesService.findById(1L)).thenReturn(category);

        Transaction savedTransaction = new Transaction();

        savedTransaction.setId(1L);
        savedTransaction.setUser(user);
        savedTransaction.setCategory(category);
        savedTransaction.setAvatar("/test/path");
        savedTransaction.setDate(date);
        savedTransaction.setName("Uber");
        savedTransaction.setAmount(new BigDecimal(1000));
        savedTransaction.setRecurring(false);

        when(repository.save(any(Transaction.class))).thenReturn(savedTransaction);

        TransactionsDto result = transactionsService.saveTransaction(jwt, inputData);

        assertEquals("/test/path", result.avatar());
        assertEquals("Uber", result.name());
        assertEquals(1L, result.categoryId());
        assertEquals(date, result.date());
        assertEquals(new BigDecimal(1000), result.amount());
        assertFalse(result.recurring());

        verify(usersService, times(1)).findUser(any(String.class));
        verify(jwt, times(1)).getClaim(any(String.class));
        verify(repository, times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Return all budget transactions for user")
    void getBudgetTransactionsTest() {

        Jwt jwt = mockJwt();
        AppUser user = mockUser();
        Category category = mockCategory();

        when(usersService.findUser("user123")).thenReturn(user);

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setUser(user);
        transaction.setName("Uber");
        transaction.setCategory(category);

        when(repository.findByUserIdOrderByDateDesc(1L))
                .thenReturn(List.of(transaction));

        List<TransactionResponseDto> result =
                transactionsService.getBudgetTransactions(jwt);

        assertEquals(1, result.size());
        assertEquals("Uber", result.getFirst().name());

        verify(repository).findByUserIdOrderByDateDesc(1L);
    }

    @Test
    @DisplayName("Edit transaction successfully")
    void editTransactionSuccessTest() {

        Jwt jwt = mockJwt();
        AppUser user = mockUser();
        Category category = mockCategory();

        when(usersService.findUser("user123")).thenReturn(user);
        when(categoriesService.findById(1L)).thenReturn(category);

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setUser(user);

        when(repository.findByIdAndUser_Id(1L, 1L))
                .thenReturn(Optional.of(transaction));

        when(repository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TransactionsDto update =
                new TransactionsDto("/img", "Netflix", 1L,
                        LocalDate.now(), new BigDecimal("200"), false);

        TransactionResponseDto result =
                transactionsService.editTransaction(jwt, 1L, update);

        assertEquals("Netflix", result.name());

        verify(repository).save(transaction);
    }

    @Test
    @DisplayName("Throw exception when transaction not found")
    void editTransactionNotFoundTest() {

        Jwt jwt = mockJwt();
        AppUser user = mockUser();

        when(usersService.findUser("user123")).thenReturn(user);

        when(repository.findByIdAndUser_Id(1L, 1L))
                .thenReturn(Optional.empty());

        TransactionsDto update =
                new TransactionsDto("/img", "Netflix", 1L,
                        LocalDate.now(), new BigDecimal("200"), false);

        assertThrows(ResponseStatusException.class,
                () -> transactionsService.editTransaction(jwt, 1L, update));
    }

    @Test
    @DisplayName("Delete transaction by id and user")
    void deleteTransactionTest() {

        Jwt jwt = mockJwt();
        AppUser user = mockUser();

        when(usersService.findUser("user123")).thenReturn(user);

        transactionsService.deleteTransaction(jwt, 10L);

        verify(repository).deleteByIdAndUser_Id(10L, 1L);
    }

    @Test
    @DisplayName("Return absolute total spent by category")
    void getTotalSpentByCategoryTest() {

        when(repository.getTotalByUserAndCategory(1L, 2L))
                .thenReturn(new BigDecimal("-500"));

        BigDecimal result =
                transactionsService.getTotalSpentByCategory(1L, 2L);

        assertEquals(new BigDecimal("500"), result);
    }

    @Test
    @DisplayName("Return paginated transactions")
    void getAllUserTransactionsTest() {

        Jwt jwt = mockJwt();
        AppUser user = mockUser();
        Category category = mockCategory();

        when(usersService.findUser("user123")).thenReturn(user);

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setUser(user);
        transaction.setCategory(category);

        Page<Transaction> page =
                new PageImpl<>(List.of(transaction));

        when(repository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(page);

        TransactionPageResponseDto result =
                transactionsService.getAllUserTransactions(
                        jwt,
                        0,
                        TransactionSort.LATEST,
                        null,
                        null
                );

        assertEquals(1, result.transactions().size());
    }

}
