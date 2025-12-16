package com.cfloresh.springboot.app.personalfinance.mapper;

import com.cfloresh.springboot.app.personalfinance.dto.TransactionsDto;
import com.cfloresh.springboot.app.personalfinance.model.transactions.Transaction;

public class TransactionsMapper {

    public static TransactionsDto toDto(Transaction transaction) {
        return new TransactionsDto(
                transaction.getAvatar(),
                transaction.getName(),
                transaction.getCategory(),
                transaction.getDate(),
                transaction.getAmount(),
                transaction.isRecurring());
    }
}
