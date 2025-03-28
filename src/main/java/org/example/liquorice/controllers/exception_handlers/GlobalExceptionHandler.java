package org.example.liquorice.controllers.exception_handlers;

import com.stripe.exception.StripeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StripeException.class)
    public ResponseEntity<Object> handleStripeException(StripeException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Payment processing error: " + ex.getMessage());
        body.put("error", ex.getClass().getSimpleName());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Invalid arguments passed: " + ex.getMessage());
        body.put("error", ex.getClass().getSimpleName());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}