package com.cfloresh.springboot.app.personalfinance.controller.budgets;

import com.cfloresh.springboot.app.personalfinance.dto.budgets.BudgetDto;
import com.cfloresh.springboot.app.personalfinance.dto.budgets.BudgetResponseDto;
import com.cfloresh.springboot.app.personalfinance.service.BudgetsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetsController {

    private final BudgetsService service;

    public BudgetsController(BudgetsService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponseDto>> getBudgets(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(service.getBudgets(jwt));
    }

    @PostMapping
    public ResponseEntity<BudgetResponseDto> createBudget(@AuthenticationPrincipal Jwt jwt,
                                                          @RequestBody BudgetDto budgetData) {
        return ResponseEntity.ok(service.createBudget(jwt, budgetData));
    }
}
