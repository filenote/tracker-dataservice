package org.example.tracker.exception;

public class ElementNotFound extends RuntimeException {
    private static final long serialVersionUID = 609638615606938487L;

    public ElementNotFound(String message) {
        super(message);
    }
}
