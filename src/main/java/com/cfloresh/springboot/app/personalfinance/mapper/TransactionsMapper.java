package com.cfloresh.springboot.app.personalfinance.mapper;

import com.cfloresh.springboot.app.personalfinance.dto.category.CategoryResponseDto;
import com.cfloresh.springboot.app.personalfinance.dto.transactions.TransactionsDto;
import com.cfloresh.springboot.app.personalfinance.dto.transactions.TransactionResponseDto;
import com.cfloresh.springboot.app.personalfinance.model.transactions.Transaction;

public class TransactionsMapper {

    public static TransactionsDto toDto(Transaction transaction) {
        return new TransactionsDto(
                transaction.getAvatar(),
                transaction.getName(),
                transaction.getCategory().getId(),
                transaction.getDate(),
                transaction.getAmount(),
                transaction.isRecurring());
    }

        public static TransactionResponseDto toResponseDto(Transaction transaction) {
            CategoryResponseDto categoryDto =
                    CategoryMapper.toResponseDto(transaction.getCategory());

        return new TransactionResponseDto(
                transaction.getId(),
                transaction.getAvatar(),
                transaction.getName(),
                categoryDto,
                transaction.getDate(),
                transaction.getAmount(),
                transaction.isRecurring());
    }
}
