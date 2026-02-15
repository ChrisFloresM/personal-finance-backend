package com.cfloresh.springboot.app.personalfinance.repository.transactions;

import com.cfloresh.springboot.app.personalfinance.dto.overview.OverviewProjection;
import com.cfloresh.springboot.app.personalfinance.model.transactions.Transaction;
import com.cfloresh.springboot.app.personalfinance.model.users.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransactionsRespository extends JpaRepository<Transaction, Long>,
        JpaSpecificationExecutor<Transaction> {
    public Optional<Transaction> findByIdAndUser_Id(Long transactionId, Long userId);

    public void deleteByIdAndUser_Id(Long transactionId, Long userId);

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.user.id = ?1
                AND t.amount < 0
                AND t.category.id = ?2
            """)
    public BigDecimal getTotalByUserAndCategory(Long userId, Long categoryId);

    public List<Transaction> findByUserIdOrderByDateDesc(Long userId);

    @Query("""
            SELECT 
                COALESCE(SUM(t.amount), 0) AS balance,
                COALESCE(SUM(CASE WHEN t.amount > 0 THEN t.amount ELSE 0 END), 0) AS income,
                COALESCE(SUM(CASE WHEN t.amount < 0 THEN t.amount ELSE 0 END), 0) AS expenses
                FROM Transaction t
                WHERE t.user.id = :userId
            """)
    OverviewProjection getOverviewData(Long userId);
}
