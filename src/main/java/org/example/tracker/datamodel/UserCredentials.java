package org.example.tracker.datamodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentials {
    private String username;
    private String password;
    private List<SimplerGrantedAuthority> authorities;
}
