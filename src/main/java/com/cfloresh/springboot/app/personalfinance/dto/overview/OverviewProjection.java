package com.cfloresh.springboot.app.personalfinance.dto.overview;

import java.math.BigDecimal;

public interface OverviewProjection {

    BigDecimal getBalance();
    BigDecimal getIncome();
    BigDecimal getExpenses();
}
