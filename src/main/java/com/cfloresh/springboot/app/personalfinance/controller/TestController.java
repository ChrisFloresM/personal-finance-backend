package com.cfloresh.springboot.app.personalfinance.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/private")
    public String testPrivate() {
        return "Test private";
    }

    @GetMapping("/api/public")
    public String testPublic() {
        return "Test public";
    }

    @GetMapping("/me")
    public String me(@AuthenticationPrincipal Jwt jwt) {
        return jwt.getClaim("sub");
    }
}
