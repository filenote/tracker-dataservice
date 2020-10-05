package org.example.tracker.handler;

import org.example.tracker.datamodel.response.FailureResponse;
import org.example.tracker.exception.ElementNotFound;
import org.example.tracker.exception.FailedToInsert;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler(value = ElementNotFound.class)
    public ResponseEntity<?> handleElementNotFound() {
        return ResponseEntity
                .noContent()
                .build();
    }

    @ExceptionHandler(value = FailedToInsert.class)
    public ResponseEntity<FailureResponse> handleFailedToInsert() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(FailureResponse.message("Failed to insert data."));
    }
}
