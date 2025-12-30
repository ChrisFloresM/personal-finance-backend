package com.cfloresh.springboot.app.personalfinance.repository.pots;

import com.cfloresh.springboot.app.personalfinance.model.pots.Pot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PotsRepository extends JpaRepository<Pot, Long> {

    public List<Pot> findAllByUserId(Long userId);
}
