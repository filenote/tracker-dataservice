package org.example.tracker.exception;

public class UsernameAlreadyExists extends ConflictException {
    public UsernameAlreadyExists() {
        super("Username already exists.");
    }
}
