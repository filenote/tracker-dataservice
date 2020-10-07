package org.example.tracker.datamodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimplerGrantedAuthority implements GrantedAuthority {

    private static final long serialVersionUID = 7245717008600704211L;
    private Role role;

    public static SimplerGrantedAuthority of(Role role) {
        return new SimplerGrantedAuthority(role);
    }

    public static SimplerGrantedAuthority of(String string) {
        return new SimplerGrantedAuthority(Role.parseStringValue(string));
    }

    @Override
    public String getAuthority() {
        return this.role.stringValue();
    }
}
