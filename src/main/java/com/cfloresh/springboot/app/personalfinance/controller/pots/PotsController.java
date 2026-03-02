package com.cfloresh.springboot.app.personalfinance.controller.pots;

import com.cfloresh.springboot.app.personalfinance.dto.pots.PotDto;
import com.cfloresh.springboot.app.personalfinance.dto.pots.PotResponseDto;
import com.cfloresh.springboot.app.personalfinance.service.pots.PotsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/pots")
@Slf4j
public class PotsController {

    private final PotsService service;

    public PotsController(PotsService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PotDto> savePot(@AuthenticationPrincipal Jwt jwt,
                                          @Valid @RequestBody PotDto potData) {
        String auth0User = jwt.getClaim("sub");
        log.info("POST /api/pots Auth0 user={} request received - potName={}",
                auth0User, potData.name());

        PotDto response = service.savePot(jwt, potData);

        log.info("POST /api/pots completed with status = 201, Auth0 user={}", auth0User);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PotResponseDto>> getPots(@AuthenticationPrincipal Jwt jwt) {
        String auth0User = jwt.getClaim("sub");
        log.info("GET /api/pots Auth0 user={} request received", auth0User);

        List<PotResponseDto> response = service.getPots(jwt);

        log.info("GET /api/pots completed with status = 200, Auth0 user={}", auth0User);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{potId}")
    public ResponseEntity<PotResponseDto> editPot(@AuthenticationPrincipal Jwt jwt,
                                          @PathVariable Long potId,
                                          @Valid @RequestBody PotDto potData) {
        String auth0User = jwt.getClaim("sub");
        log.info("PUT /api/pots/{} Auth0 user={} request received", potId, auth0User);

        PotResponseDto response = service.editPot(jwt, potId, potData);

        log.info("PUT /api/pots/{} completed with status = 201, Auth0 user={}", potId, auth0User);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{potId}")
    public ResponseEntity<String> deletePot(@AuthenticationPrincipal Jwt jwt,
                                          @PathVariable Long potId ) {
        String auth0User = jwt.getClaim("sub");
        log.info("DELETE /api/pots/{} Auth0 user={} request received", potId, auth0User);

        service.deletePot(jwt, potId);

        log.info("DELETE /api/pots/{} completed with status = 204, Auth0 user={}", potId, auth0User);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Pot successfully deleted");
    }
}
