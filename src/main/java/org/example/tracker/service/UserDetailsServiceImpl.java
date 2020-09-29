package org.example.tracker.service;

import org.example.tracker.datamodel.UserCredentials;
import org.example.tracker.repository.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserCredentialsRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredentials credentials = repository.findUserCredentialsByUsername(username);
        if (credentials == null) {
            throw new UsernameNotFoundException(username);
        } else {
            // last parameter is role
            User user = new User(credentials.getUsername(), credentials.getPassword(), new ArrayList<>());
            return user;
        }
    }
}
