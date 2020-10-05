package org.example.tracker.exception;

public class ConflictException extends RuntimeException {
    private static final long serialVersionUID = 3321058550100104669L;

    public ConflictException(String message) {
        super(message);
    }
}
