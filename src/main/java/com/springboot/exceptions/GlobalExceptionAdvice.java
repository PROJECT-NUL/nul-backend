package com.springboot.exceptions;

import com.springboot.utils.ExceptionMessageAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionAdvice {

    private static final String INTERNAL_SERVER_ERROR = "internal_server_error";

    private final ExceptionMessageAccessor exceptionMessageAccessor;

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiExceptionResponse> handleAllExceptions(Exception exception) {
        log.error("Unhandled exception", exception);
        String message = exceptionMessageAccessor.getMessage(null, INTERNAL_SERVER_ERROR);
        ApiExceptionResponse response = new ApiExceptionResponse(
                message,
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now()
        );
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
