package com.cfloresh.springboot.app.personalfinance.controller.transactions;

import com.cfloresh.springboot.app.personalfinance.dto.transactions.TransactionPageResponseDto;
import com.cfloresh.springboot.app.personalfinance.dto.transactions.TransactionsDto;
import com.cfloresh.springboot.app.personalfinance.dto.transactions.TransactionResponseDto;
import com.cfloresh.springboot.app.personalfinance.model.transactions.TransactionSort;
import com.cfloresh.springboot.app.personalfinance.service.transactions.TransactionsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/transactions")
@Slf4j
public class TransactionsController {

    private final TransactionsService service;

    public TransactionsController(TransactionsService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<TransactionsDto> registerTransaction(@AuthenticationPrincipal Jwt jwt,
                                                      @Valid @RequestBody TransactionsDto data) {
        String auth0User = jwt.getClaim("sub");
        log.info("POST /transactions Auth0 user={} request received", auth0User);

        TransactionsDto response = service.saveTransaction(jwt, data);

        log.info("POST /transactions completed with status = 201, Auth0 user={}", auth0User);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping()
    public ResponseEntity<TransactionPageResponseDto> getAllUserTransactions(@AuthenticationPrincipal Jwt jwt,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "LATEST") TransactionSort sortBy,
                                                                             @RequestParam(defaultValue= "0") Long categoryId,
                                                                             @RequestParam(defaultValue="") String search) {
        String auth0User = jwt.getClaim("sub");
        log.info("GET /transactions Auth0 user={} request received - page={}, sortBy={}, categoryId={}, search={}",
                auth0User, page, sortBy, categoryId, search);

        TransactionPageResponseDto response = service.getAllUserTransactions(jwt,
                page, sortBy, categoryId, search);

        log.info("GET /transactions completed with status = 200, Auth0 user={}", auth0User);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(@AuthenticationPrincipal Jwt jwt,
                                                             @PathVariable Long transactionId,
                                                             @RequestBody TransactionsDto data) {
        String auth0User = jwt.getClaim("sub");
        log.info("PUT /transactions/{} Auth0 user={} request received", transactionId, auth0User);

        TransactionResponseDto response = service.editTransaction(jwt, transactionId, data);

        log.info("PUT /transactions/{} completed with status = 201, Auth0 user={}", transactionId, auth0User);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteById(@AuthenticationPrincipal Jwt jwt,
                                           @PathVariable Long transactionId) {
        String auth0User = jwt.getClaim("sub");
        log.info("DELETE /transactions/{} Auth0 user={} request received", transactionId, auth0User);

        service.deleteTransaction(jwt, transactionId);

        log.info("DELETE /transactions/{} completed with status = 204, Auth0 user={}", transactionId, auth0User);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
