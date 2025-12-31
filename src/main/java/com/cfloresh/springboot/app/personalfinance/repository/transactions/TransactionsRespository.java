package com.cfloresh.springboot.app.personalfinance.repository.transactions;

import com.cfloresh.springboot.app.personalfinance.model.transactions.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TransactionsRespository extends JpaRepository<Transaction, Long>,
        JpaSpecificationExecutor<Transaction> {
    public Optional<Transaction> findByIdAndUser_Id(Long transactionId, Long userId);

    public void deleteByIdAndUser_Id(Long transactionId, Long userId);
}
