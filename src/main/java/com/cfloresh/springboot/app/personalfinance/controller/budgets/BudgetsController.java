package com.cfloresh.springboot.app.personalfinance.controller.budgets;

import com.cfloresh.springboot.app.personalfinance.dto.budgets.BudgetDto;
import com.cfloresh.springboot.app.personalfinance.dto.budgets.BudgetResponseDto;
import com.cfloresh.springboot.app.personalfinance.dto.budgets.BudgetResponseWListDto;
import com.cfloresh.springboot.app.personalfinance.service.budgets.BudgetsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@Slf4j
public class BudgetsController {

    private final BudgetsService service;

    public BudgetsController(BudgetsService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponseDto>> getBudgets(@AuthenticationPrincipal Jwt jwt) {
        String auth0User = jwt.getClaim("sub");
        log.info("GET /api/budgets Auth0 user={} request received", auth0User);

        List<BudgetResponseDto> response = service.getBudgets(jwt);

        log.info("GET /api/budgets completed with status = 200, Auth0 user={}", auth0User);
        return ResponseEntity.ok(response);
    }

    @GetMapping("overview")
    public ResponseEntity<List<BudgetResponseWListDto>> getBudgetsWList(@AuthenticationPrincipal Jwt jwt) {
        String auth0User = jwt.getClaim("sub");
        log.info("GET /api/budgets/overview Auth0 user={} request received", auth0User);

        List<BudgetResponseWListDto> response = service.getBudgetsWList(jwt);

        log.info("GET /api/budgets/overview completed with status = 200, Auth0 user={}", auth0User);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BudgetResponseDto> createBudget(@AuthenticationPrincipal Jwt jwt,
                                                          @RequestBody BudgetDto budgetData) {
        String auth0User = jwt.getClaim("sub");
        log.info("POST /api/budgets Auth0 user={} request received - categoryId={}",
                auth0User, budgetData.categoryId());

        BudgetResponseDto response = service.createBudget(jwt, budgetData);

        log.info("POST /api/budgets completed with status = 200, Auth0 user={}", auth0User);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<BudgetResponseDto> editBudget(@AuthenticationPrincipal Jwt jwt,
                                                        @PathVariable Long budgetId,
                                                        @Valid @RequestBody BudgetDto budgetData) {
        String auth0User = jwt.getClaim("sub");
        log.info("PUT /api/budgets/{} Auth0 user={} request received", budgetId, auth0User);

        BudgetResponseDto response = service.editBudget(jwt, budgetId, budgetData);

        log.info("PUT /api/budgets/{} completed with status = 201, Auth0 user={}", budgetId, auth0User);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<String> deleteBudget(@AuthenticationPrincipal Jwt jwt,
                                               @PathVariable Long budgetId) {
        String auth0User = jwt.getClaim("sub");
        log.info("DELETE /api/budgets/{} Auth0 user={} request received", budgetId, auth0User);

        service.deleteBudget(jwt, budgetId);

        log.info("DELETE /api/budgets/{} completed with status = 204, Auth0 user={}", budgetId, auth0User);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Budget successfully deleted");
    }
}
