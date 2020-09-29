package org.example.tracker.repository;

import org.example.tracker.datamodel.UserCredentials;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserCredentialsRepository {

    private static List<UserCredentials> userCredentials;
    static {
        userCredentials = new ArrayList<>();
    }

    public UserCredentials findUserCredentialsByUsername(String username) {
        System.out.println(username);
        return userCredentials.stream().filter(creds -> creds.getUsername().equals(username)).findFirst().orElse(null);
    }

    public boolean addAccount(UserCredentials credentials) {
        Optional<UserCredentials> first = userCredentials.stream().filter(creds -> creds.getUsername().equals(credentials.getUsername())).findFirst();
        if (first.isPresent()) return false;
        userCredentials.add(credentials);
        return true;
    }
}
