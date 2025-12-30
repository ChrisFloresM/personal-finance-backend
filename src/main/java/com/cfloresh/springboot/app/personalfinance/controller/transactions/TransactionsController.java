package com.cfloresh.springboot.app.personalfinance.controller.transactions;

import com.cfloresh.springboot.app.personalfinance.dto.transactions.TransactionPageResponseDto;
import com.cfloresh.springboot.app.personalfinance.dto.transactions.TransactionsDto;
import com.cfloresh.springboot.app.personalfinance.dto.transactions.TransactionResponseDto;
import com.cfloresh.springboot.app.personalfinance.model.transactions.TransactionSort;
import com.cfloresh.springboot.app.personalfinance.service.transactions.TransactionsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<TransactionPageResponseDto> getAllUserTransactions(@AuthenticationPrincipal Jwt jwt,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "LATEST") TransactionSort sortBy,
                                                                             @RequestParam(defaultValue="ALL") String category,
                                                                             @RequestParam(defaultValue="") String search) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllUserTransactions(jwt,
                page, sortBy, category, search));
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
