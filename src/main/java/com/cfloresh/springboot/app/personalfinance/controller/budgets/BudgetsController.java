package com.cfloresh.springboot.app.personalfinance.controller.budgets;

import com.cfloresh.springboot.app.personalfinance.dto.budgets.BudgetDto;
import com.cfloresh.springboot.app.personalfinance.dto.budgets.BudgetResponseDto;
import com.cfloresh.springboot.app.personalfinance.dto.budgets.BudgetResponseWListDto;
import com.cfloresh.springboot.app.personalfinance.service.BudgetsService;
import jakarta.validation.Valid;
import org.aspectj.weaver.ast.Call;
import org.springframework.http.HttpStatus;
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

    @GetMapping("overview")
    public ResponseEntity<List<BudgetResponseWListDto>> getBudgetsWList(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(service.getBudgetsWList(jwt));
    }

    @PostMapping
    public ResponseEntity<BudgetResponseDto> createBudget(@AuthenticationPrincipal Jwt jwt,
                                                          @RequestBody BudgetDto budgetData) {
        return ResponseEntity.ok(service.createBudget(jwt, budgetData));
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<BudgetResponseDto> editBudget(@AuthenticationPrincipal Jwt jwt,
                                                        @PathVariable Long budgetId,
                                                        @Valid @RequestBody BudgetDto budgetData) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.editBudget(jwt, budgetId,
                budgetData));
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<String> deleteBudget(@AuthenticationPrincipal Jwt jwt,
                                               @PathVariable Long budgetId) {

        service.deleteBudget(jwt, budgetId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Budget successfully deleted");
    }
}
