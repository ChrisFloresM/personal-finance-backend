package com.cfloresh.springboot.app.personalfinance.service.transactions;

import com.cfloresh.springboot.app.personalfinance.repository.transactions.TransactionsRespository;
import org.springframework.stereotype.Service;

@Service
public class TransactionsService {
    private final TransactionsRespository repository;

    public TransactionsService(TransactionsRespository repository) {
        this.repository = repository;
    }

}
