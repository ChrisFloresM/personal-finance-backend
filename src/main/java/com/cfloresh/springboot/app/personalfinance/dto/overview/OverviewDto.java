package com.cfloresh.springboot.app.personalfinance.dto.overview;

import java.math.BigDecimal;

public record OverviewDto(BigDecimal balance, BigDecimal income, BigDecimal expenses) {
}
