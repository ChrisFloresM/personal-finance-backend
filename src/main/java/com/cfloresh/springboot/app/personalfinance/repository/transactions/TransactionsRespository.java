package com.cfloresh.springboot.app.personalfinance.repository.transactions;

import com.cfloresh.springboot.app.personalfinance.model.transactions.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionsRespository extends JpaRepository<Transaction, Long> {
    public List<Transaction> findAllByUserId(Long userId);

    public Optional<Transaction> findByIdAndUser_Id(Long transactionId, Long userId);

    public void deleteByIdAndUser_Id(Long transactionId, Long userId);
}
