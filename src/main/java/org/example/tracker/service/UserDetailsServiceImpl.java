package org.example.tracker.service;

import org.example.tracker.datamodel.Role;
import org.example.tracker.datamodel.UserCredentials;
import org.example.tracker.repository.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
            if (CollectionUtils.isEmpty(credentials.getAuthorities())) {
                credentials = repository.addAuthority(credentials.getUsername(), Role.USER, true);
            }
            User user = new User(credentials.getUsername(), credentials.getPassword(), credentials.getAuthorities());
            return user;
        }
    }
}
