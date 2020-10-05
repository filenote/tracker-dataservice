package org.example.tracker.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.io.Resources;
import org.example.tracker.datamodel.Comment;
import org.example.tracker.datamodel.UserCredentials;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class UserCredentialsRepository {

    private static List<UserCredentials> userCredentials;
    static {
        try {
            ObjectMapper o = new ObjectMapper().registerModule(new Jdk8Module()).registerModule(new JavaTimeModule());
            URL resource = Resources.getResource("users_test_data.json");
            String s = Resources.toString(resource, Charset.defaultCharset());
            UserCredentials[] array = o.readValue(s, UserCredentials[].class);
            userCredentials = new ArrayList<>(Arrays.asList(array));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
