package com.cfloresh.springboot.app.personalfinance.service.transactions;

import com.cfloresh.springboot.app.personalfinance.dto.transactions.TransactionsDto;
import com.cfloresh.springboot.app.personalfinance.model.categories.Category;
import com.cfloresh.springboot.app.personalfinance.model.transactions.Transaction;
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
import org.springframework.security.oauth2.jwt.Jwt;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Test
    @DisplayName("Save a transaction to DB and return DTO")
    void saveTransactionTest() {
        LocalDate date = LocalDate.now();

        // Define the inputs for the method
        TransactionsDto inputData = new TransactionsDto("/test/path", "Uber", 1L, date,
                new BigDecimal(1000), false);

        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaim("sub")).thenReturn("user123");


        // Define the expected outputs
        AppUser user = new AppUser();
        user.setId(1L);
        user.setAuthId("user123");
        user.setCreatedAt(Instant.now());

        Category category = new Category();
        category.setId(1L);
        category.setKey("general");
        category.setLabel("General");

        when(usersService.findUser("user123")).thenReturn(user);

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

        // Call the Unit under test
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

}
