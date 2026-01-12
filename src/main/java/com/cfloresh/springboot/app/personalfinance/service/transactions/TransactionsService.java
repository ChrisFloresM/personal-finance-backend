package com.cfloresh.springboot.app.personalfinance.service.transactions;

import com.cfloresh.springboot.app.personalfinance.dto.transactions.TransactionPageResponseDto;
import com.cfloresh.springboot.app.personalfinance.dto.transactions.TransactionsDto;
import com.cfloresh.springboot.app.personalfinance.dto.transactions.TransactionResponseDto;
import com.cfloresh.springboot.app.personalfinance.mapper.TransactionsMapper;
import com.cfloresh.springboot.app.personalfinance.model.transactions.Transaction;
import com.cfloresh.springboot.app.personalfinance.model.transactions.TransactionSort;
import com.cfloresh.springboot.app.personalfinance.model.transactions.TransactionsSpeficiations;
import com.cfloresh.springboot.app.personalfinance.model.users.AppUser;
import com.cfloresh.springboot.app.personalfinance.repository.transactions.TransactionsRespository;
import com.cfloresh.springboot.app.personalfinance.service.categories.CategoriesService;
import com.cfloresh.springboot.app.personalfinance.service.users.UsersService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class TransactionsService {
    private final TransactionsRespository repository;
    private final UsersService usersService;
    private final CategoriesService categoriesService;

    public TransactionsService(TransactionsRespository repository, UsersService usersService, CategoriesService categoriesService) {
        this.repository = repository;
        this.usersService = usersService;
        this.categoriesService = categoriesService;
    }

    public TransactionsDto saveTransaction(Jwt jwt, TransactionsDto data) {
        /* 1. Find the user in the DB based on the received sub, if not exists, register */
        AppUser user = usersService.findUser(jwt.getClaim("sub"));

        /* Extract the data from dto and create a new transaction item */
        Transaction transaction = new Transaction();

        transaction.setUser(user);
        setTransactionData(transaction, data);

        Transaction savedTransaction = repository.save(transaction);

        return TransactionsMapper.toDto(savedTransaction);
    }

    public TransactionPageResponseDto getAllUserTransactions(Jwt jwt, int pageNumber,
                                                             TransactionSort sortBy,
                                                             Long categoryId, String search) {
        int PAGE_SIZE = 9;

        AppUser user = usersService.findUser(jwt.getClaim("sub"));

        /* specification */
        var specification =
                Specification.where(TransactionsSpeficiations.hasUserId(user.getId()))
                        .and(TransactionsSpeficiations.hasCategory(categoryId))
                        .and(TransactionsSpeficiations.includeSearch(search));

        Page<Transaction> pageTransactions = repository.findAll(specification,
                PageRequest.of(pageNumber, PAGE_SIZE, sortBy.getSort()));

        int totalPages = pageTransactions.getTotalPages();

        return new TransactionPageResponseDto(pageTransactions.stream().map(TransactionsMapper::toResponseDto).toList(), totalPages);
    }


    public TransactionResponseDto editTransaction(Jwt jwt, Long transactionId,
                                                  TransactionsDto data) {
        AppUser user = usersService.findUser(jwt.getClaim("sub"));

        Transaction transaction =
                repository.findByIdAndUser_Id(transactionId, user.getId()).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Transaction not found"));


        setTransactionData(transaction, data);
        Transaction savedTransaction = repository.save(transaction);

        return TransactionsMapper.toResponseDto(savedTransaction);
    }

    @Transactional
    public void deleteTransaction(Jwt jwt, Long transactionId) {

        AppUser user = usersService.findUser(jwt.getClaim("sub"));

        repository.deleteByIdAndUser_Id(transactionId, user.getId());
    }

    public BigDecimal getTotalSpentByCategory(Long userId, Long categoryId) {
        return repository.getTotalByUserAndCategory(userId, categoryId);
    }

    private void setTransactionData(Transaction transaction, TransactionsDto data) {

        transaction.setAvatar(data.avatar());
        transaction.setName(data.name());
        transaction.setCategory(categoriesService.findById(data.categoryId()));
        transaction.setDate(data.date());
        transaction.setAmount(data.amount());
        transaction.setRecurring(data.recurring());
    }

}
