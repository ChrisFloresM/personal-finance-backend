package com.cfloresh.springboot.app.personalfinance.controller.overview;

import com.cfloresh.springboot.app.personalfinance.dto.overview.OverviewDto;
import com.cfloresh.springboot.app.personalfinance.service.overview.OverviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/overview")
public class OverviewController {

    private final OverviewService service;

    public OverviewController(OverviewService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<OverviewDto> getUserOverview(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(service.getOverview(jwt));
    }
}
