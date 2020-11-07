package org.example.tracker.controller;

import org.example.tracker.datamodel.AccountInformation;
import org.example.tracker.datamodel.Role;
import org.example.tracker.datamodel.SimplerGrantedAuthority;
import org.example.tracker.datamodel.UserCredentials;
import org.example.tracker.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService service;

    @PostMapping("/register")
    public boolean register(@RequestBody UserCredentials credentials) {
        return service.registerAccount(credentials);
    }

    @GetMapping
    public AccountInformation getAccountInformation(Authentication authentication) {
        return service.getAccountInformation(authentication);
    }

    @GetMapping("/auth")
    public ResponseEntity<Boolean> isAuthorized(@RequestParam(defaultValue = "USER") Role role, Authentication authentication) {
        boolean isAuthorized = authentication.getAuthorities().contains(SimplerGrantedAuthority.of(role));

        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        return ResponseEntity.ok(true);
    }
}
