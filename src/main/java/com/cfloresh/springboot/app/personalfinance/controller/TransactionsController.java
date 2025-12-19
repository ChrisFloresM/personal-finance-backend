package com.cfloresh.springboot.app.personalfinance.controller;

import com.cfloresh.springboot.app.personalfinance.dto.TransactionsDto;
import com.cfloresh.springboot.app.personalfinance.dto.TransactionResponseDto;
import com.cfloresh.springboot.app.personalfinance.service.transactions.TransactionsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/transactions")
@CrossOrigin(origins = "http://localhost:5173")
public class TransactionsController {

    private final TransactionsService service;

    public TransactionsController(TransactionsService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<TransactionsDto> registerTransaction(@AuthenticationPrincipal Jwt jwt,
                                                      @Valid @RequestBody TransactionsDto data) {
        return ResponseEntity.ok(service.saveTransaction(jwt, data));
    }

    @GetMapping()
    public ResponseEntity<List<TransactionResponseDto>> getAllUserTransactions(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.getAllUserTransactions(jwt));
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(@AuthenticationPrincipal Jwt jwt,
                                                             @PathVariable Long transactionId,
                                                             @RequestBody TransactionsDto data) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.editTransaction(jwt, transactionId, data));
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteById(@AuthenticationPrincipal Jwt jwt,
                                           @PathVariable Long transactionId) {

        service.deleteTransaction(jwt, transactionId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
