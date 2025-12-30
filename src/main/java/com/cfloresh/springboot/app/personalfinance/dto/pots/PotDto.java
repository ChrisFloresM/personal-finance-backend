package com.cfloresh.springboot.app.personalfinance.dto.pots;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PotDto(@NotNull String name, @NotNull BigDecimal target, @NotNull BigDecimal total,
                     @NotNull String theme) {
}
