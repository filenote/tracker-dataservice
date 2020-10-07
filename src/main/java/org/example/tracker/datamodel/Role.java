package org.example.tracker.datamodel;

public enum Role {
    ADMINISTRATOR,
    USER;

    private final String stringValue;

    public static Role parseStringValue(String string) {
        if (string == null || !string.contains("ROLE_")) {
            return null;
        }

        return Role.valueOf(string.replace("ROLE_", ""));
    }

    public String stringValue() {
        return this.stringValue;
    }

    Role() {
        this.stringValue = "ROLE_" + this.toString();
    }
}
