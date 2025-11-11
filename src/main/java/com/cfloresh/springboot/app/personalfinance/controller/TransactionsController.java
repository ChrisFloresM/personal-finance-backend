package com.cfloresh.springboot.app.personalfinance.controller;

import com.cfloresh.springboot.app.personalfinance.dto.TransactionDTO;
import com.cfloresh.springboot.app.personalfinance.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin
public class TransactionsController {
    private final TransactionService transactionService;

    public TransactionsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

}
