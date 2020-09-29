package org.example.tracker.controller;

import org.example.tracker.datamodel.UserCredentials;
import org.example.tracker.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService service;

    @PostMapping("/register")
    public boolean register(@RequestBody UserCredentials credentials) {
        return service.registerAccount(credentials);
    }
}
