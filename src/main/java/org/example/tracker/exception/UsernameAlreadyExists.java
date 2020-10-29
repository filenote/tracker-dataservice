package org.example.tracker.exception;

public class UsernameAlreadyExists extends ConflictException {
    private static final long serialVersionUID = 3321058550143214669L;

    public UsernameAlreadyExists() {
        super("Username already exists.");
    }
}
