package com.cfloresh.springboot.app.personalfinance.service.transactions;

import com.cfloresh.springboot.app.personalfinance.dto.TransactionsDto;
import com.cfloresh.springboot.app.personalfinance.dto.TransactionResponseDto;
import com.cfloresh.springboot.app.personalfinance.mapper.TransactionsMapper;
import com.cfloresh.springboot.app.personalfinance.model.transactions.Transaction;
import com.cfloresh.springboot.app.personalfinance.model.users.AppUser;
import com.cfloresh.springboot.app.personalfinance.repository.transactions.TransactionsRespository;
import com.cfloresh.springboot.app.personalfinance.service.users.UsersService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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
        setTransactionData(transaction, data);

        Transaction savedTransaction = repository.save(transaction);

        return TransactionsMapper.toDto(savedTransaction);
    }

    public List<TransactionResponseDto> getAllUserTransactions(Jwt jwt) {
        AppUser user = usersService.findUser(jwt.getClaim("sub"));
        List<Transaction> transactions = repository.findAllByUserId(user.getId());

        return transactions.stream().map(TransactionsMapper::toResponseDto).toList();
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

    private void setTransactionData(Transaction transaction, TransactionsDto data) {
        transaction.setAvatar(data.avatar());
        transaction.setName(data.name());
        transaction.setCategory(data.category());
        transaction.setDate(data.date());
        transaction.setAmount(data.amount());
        transaction.setRecurring(data.recurring());
    }

}
