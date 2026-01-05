package com.cfloresh.springboot.app.personalfinance.repository.pots;

import com.cfloresh.springboot.app.personalfinance.model.pots.Pot;
import com.cfloresh.springboot.app.personalfinance.model.transactions.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PotsRepository extends JpaRepository<Pot, Long> {

    public List<Pot> findAllByUserId(Long userId);
    public Optional<Pot> findByIdAndUser_Id(Long potID, Long userId);
    public void deleteByIdAndUser_Id(Long potId, Long userId);
}
