package com.cfloresh.springboot.app.personalfinance.repository.transactions;

import com.cfloresh.springboot.app.personalfinance.model.transactions.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionsRespository extends JpaRepository<Transaction, Long> {
}
