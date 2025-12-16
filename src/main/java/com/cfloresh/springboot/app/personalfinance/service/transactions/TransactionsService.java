package com.cfloresh.springboot.app.personalfinance.service.transactions;

import com.cfloresh.springboot.app.personalfinance.dto.TransactionsDto;
import com.cfloresh.springboot.app.personalfinance.mapper.TransactionsMapper;
import com.cfloresh.springboot.app.personalfinance.model.transactions.Transaction;
import com.cfloresh.springboot.app.personalfinance.model.users.AppUser;
import com.cfloresh.springboot.app.personalfinance.repository.transactions.TransactionsRespository;
import com.cfloresh.springboot.app.personalfinance.service.users.UsersService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionsService {
    private final TransactionsRespository repository;
    private final UsersService usersService;

    public TransactionsService(TransactionsRespository repository, UsersService usersService) {
        this.repository = repository;
        this.usersService = usersService;
    }

    public TransactionsDto saveTransaction(Jwt jwt, TransactionsDto data) {
        /* 1. Find the user in the DB based on the received sub, if not exists, register */
        AppUser user = usersService.findUser(jwt.getClaim("sub"));

        /* Extract the data from dto and create a new transaction item */
        Transaction transaction = new Transaction();

        transaction.setUser(user);
        transaction.setAvatar(data.avatar());
        transaction.setName(data.name());
        transaction.setCategory(data.category());
        transaction.setDate(data.date());
        transaction.setAmount(data.amount());
        transaction.setRecurring(data.recurring());

        Transaction savedTransaction = repository.save(transaction);

        return TransactionsMapper.toDto(savedTransaction);
    }

    public List<TransactionsDto> getAllUserTransactions(Jwt jwt) {
        AppUser user = usersService.findUser(jwt.getClaim("sub"));
        List<Transaction> transactions = repository.findAllByUserId(user.getId());

        return transactions.stream().map(TransactionsMapper::toDto).toList();
    }


}
