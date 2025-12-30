package com.cfloresh.springboot.app.personalfinance.dto.transactions;

import java.util.List;

public record TransactionPageResponseDto(List<TransactionResponseDto> transactions,
                                         int totalPages) {
}
