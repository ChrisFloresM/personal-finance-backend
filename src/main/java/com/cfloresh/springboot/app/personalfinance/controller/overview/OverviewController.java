package com.cfloresh.springboot.app.personalfinance.controller.overview;

import com.cfloresh.springboot.app.personalfinance.dto.overview.OverviewDto;
import com.cfloresh.springboot.app.personalfinance.service.overview.OverviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/overview")
@Slf4j
public class OverviewController {

    private final OverviewService service;

    public OverviewController(OverviewService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<OverviewDto> getUserOverview(@AuthenticationPrincipal Jwt jwt) {
        String auth0User = jwt.getClaim("sub");
        log.info("GET /api/overview Auth0 user={} request received", auth0User);

        OverviewDto response = service.getOverview(jwt);

        log.info("GET /api/overview completed with status = 200, Auth0 user={}", auth0User);
        return ResponseEntity.ok(response);
    }
}
