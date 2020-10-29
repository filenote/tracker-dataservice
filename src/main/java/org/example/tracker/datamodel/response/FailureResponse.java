package org.example.tracker.datamodel.response;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class FailureResponse {
    private final ZonedDateTime timestamp;
    private final String message;

    private FailureResponse(String message) {
        this.timestamp = ZonedDateTime.now();
        this.message = message;
    }

    public static FailureResponse message(String message) {
        return new FailureResponse(message);
    }
}
