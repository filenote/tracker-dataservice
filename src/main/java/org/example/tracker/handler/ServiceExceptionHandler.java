package org.example.tracker.handler;

import org.example.tracker.datamodel.response.FailureResponse;
import org.example.tracker.exception.ConflictException;
import org.example.tracker.exception.ElementNotFound;
import org.example.tracker.exception.FailedToInsert;
import org.example.tracker.exception.UsernameAlreadyExists;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler(ElementNotFound.class)
    public final ResponseEntity<?> handleElementNotFound() {
        return ResponseEntity
                .noContent()
                .build();
    }

    @ExceptionHandler(FailedToInsert.class)
    public final ResponseEntity<FailureResponse> handleFailedToInsert() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(FailureResponse.message("Failed to insert data."));
    }

    @ExceptionHandler({ ConflictException.class, UsernameAlreadyExists.class })
    public final ResponseEntity<FailureResponse> handleConflictException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(FailureResponse.message(e.getMessage()));
    }
}
