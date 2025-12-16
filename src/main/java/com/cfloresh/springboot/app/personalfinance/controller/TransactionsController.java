package com.cfloresh.springboot.app.personalfinance.controller;

import com.cfloresh.springboot.app.personalfinance.dto.TransactionsDto;
import com.cfloresh.springboot.app.personalfinance.service.transactions.TransactionsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
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
    public ResponseEntity<List<TransactionsDto>> getAllUserTransactions(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(service.getAllUserTransactions(jwt));
    }
}
