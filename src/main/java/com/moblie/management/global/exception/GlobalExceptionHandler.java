package com.moblie.management.global.exception;

import com.moblie.management.global.utils.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Response> handleCustomException(CustomException exception) {

        Response response = new Response(
                exception.getErrorCode().getHttpStatus(),
                exception.getErrorCode().getMessage(),
                List.of(exception.getErrorMessage())
        );

        return ResponseEntity.status(exception.getErrorCode().getHttpStatus().value()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Response> handleValidationException(MethodArgumentNotValidException e) {
        List<String> errors = e.getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());

        Response response = new Response(
                HttpStatus.BAD_REQUEST,
                "사용자 요청 실패",
                errors
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleGenericException(Exception e) {
        Response response = new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                null
        );

        return ResponseEntity.status(500).body(response);
    }

}
