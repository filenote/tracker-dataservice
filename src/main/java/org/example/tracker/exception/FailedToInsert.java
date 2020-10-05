package org.example.tracker.exception;

public class FailedToInsert extends RuntimeException {
    private static final long serialVersionUID = 9017153060662165739L;

    public FailedToInsert(String message) {
        super(message);
    }
}
