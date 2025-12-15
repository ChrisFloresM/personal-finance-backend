package com.cfloresh.springboot.app.personalfinance.controller.users;

import com.cfloresh.springboot.app.personalfinance.service.users.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService service;

    public UsersController(UsersService service) {
        this.service = service;
    }

    @GetMapping("/register")
    public ResponseEntity<String> registerUser(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(service.registerUser(jwt));
    }
}
