package org.example.tracker.service;

import com.google.common.collect.ImmutableList;
import org.example.tracker.datamodel.AccountInformation;
import org.example.tracker.datamodel.Role;
import org.example.tracker.datamodel.SimplerGrantedAuthority;
import org.example.tracker.datamodel.UserCredentials;
import org.example.tracker.exception.UsernameAlreadyExists;
import org.example.tracker.repository.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private UserCredentialsRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private static final List<SimplerGrantedAuthority> defaultRoles = ImmutableList.of(SimplerGrantedAuthority.of(Role.USER));

    public boolean registerAccount(UserCredentials credentials) {
        boolean usernameExists = repository.doesUsernameExist(credentials.getUsername());
        if (usernameExists) {
            throw new UsernameAlreadyExists();
        }
        String encodedPassword = encoder.encode(credentials.getPassword());
        credentials.setPassword(encodedPassword);
        credentials.setAuthorities(List.copyOf(defaultRoles));
        return repository.addAccount(credentials);
    }

    public AccountInformation getAccountInformation(Authentication authentication) {
        UserCredentials userCredentials = repository.findUserCredentialsByUsername(authentication.getName());
        AccountInformation information = new AccountInformation();
        information.setUsername(userCredentials.getUsername());
        information.setEmail(null);
        return information;
    }
}

