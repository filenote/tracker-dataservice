package org.example.tracker.service;

import org.example.tracker.datamodel.UserCredentials;
import org.example.tracker.repository.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private UserCredentialsRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public boolean registerAccount(UserCredentials credentials) {
        String encodedPassword = encoder.encode(credentials.getPassword());
        credentials.setPassword(encodedPassword);
        return repository.addAccount(credentials);
    }
}

