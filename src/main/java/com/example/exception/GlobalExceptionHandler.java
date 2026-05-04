package com.example.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.api.APIResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 — entity not found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<APIResponse<Void>> handleEntityNotFound(
            EntityNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(APIResponse.error(ex.getMessage()));
    }
    @ExceptionHandler(EntityAlreadyExistException.class)
    public ResponseEntity<APIResponse<Void>> handleEntityAlreadyFound(
            EntityAlreadyExistException ex) {

        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
                .body(APIResponse.error(ex.getMessage()));
    }

    // 409 — invalid state, business rule violation
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<APIResponse<Void>> handleIllegalState(
            IllegalStateException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(APIResponse.error(ex.getMessage()));
    }

    // 400 — invalid argument
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse<Void>> handleIllegalArgument(
            IllegalArgumentException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(APIResponse.error(ex.getMessage()));
    }

    // 400 — @Valid validation failures
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Void>> handleValidation(
            MethodArgumentNotValidException ex) {

        String errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(APIResponse.error(errors));
    }

    // 400 — runtime exceptions from service
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIResponse<Void>> handleRuntime(
            RuntimeException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(APIResponse.error(ex.getMessage()));
    }

    // 500 — anything unexpected
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Void>> handleGeneral(
            Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIResponse.error(
                        "Internal server error: " + ex.getMessage()));
    }
    
    @ExceptionHandler(InvalidTimeRangeException.class)
    public ResponseEntity<APIResponse<Void>> handleInvalidTimeRange(
            InvalidTimeRangeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(APIResponse.error(ex.getMessage()));
    }
    @ExceptionHandler(BookingAlreadyFoundException.class)
    public ResponseEntity<APIResponse<Void>>handleBookingAlreadyFound(BookingAlreadyFoundException ex){
    	    return ResponseEntity.status(HttpStatus.ACCEPTED).body(APIResponse.error(ex.getMessage()));
    }
    
    @ExceptionHandler(AssetAlreadyAssignedException.class)
    public ResponseEntity<APIResponse<Void>>handleAssetAlreadyAssigned(AssetAlreadyAssignedException ex){
    	    return ResponseEntity.status(HttpStatus.CONFLICT).body(APIResponse.error(ex.getMessage()));
    }
}