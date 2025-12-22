package com.cfloresh.springboot.app.personalfinance.dto;

import java.util.List;

public record TransactionPageResponseDto(List<TransactionResponseDto> transactions,
                                         int totalPages) {
}
