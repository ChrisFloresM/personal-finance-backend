package com.cfloresh.springboot.app.personalfinance.controller.pots;

import com.cfloresh.springboot.app.personalfinance.dto.pots.PotDto;
import com.cfloresh.springboot.app.personalfinance.dto.pots.PotResponseDto;
import com.cfloresh.springboot.app.personalfinance.service.pots.PotsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/pots")
@CrossOrigin(origins = "http://localhost:5173")
public class PotsController {

    private final PotsService service;

    public PotsController(PotsService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PotDto> savePot(@AuthenticationPrincipal Jwt jwt,
                                          @Valid @RequestBody PotDto potData) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.savePot(jwt, potData));
    }

    @GetMapping
    public ResponseEntity<List<PotResponseDto>> getPots(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(service.getPots(jwt));
    }

    @PutMapping("/{potId}")
    public ResponseEntity<PotResponseDto> editPot(@AuthenticationPrincipal Jwt jwt,
                                          @PathVariable Long potId,
                                          @Valid @RequestBody PotDto potData) {
        return ResponseEntity.ok(service.editPot(jwt, potId, potData));
    }

    @DeleteMapping("/{potId}")
    public ResponseEntity<String> deletePot(@AuthenticationPrincipal Jwt jwt,
                                          @PathVariable Long potId ) {
        service.deletePot(jwt, potId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Pot successfully deleted");
    }
}
