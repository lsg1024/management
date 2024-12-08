package com.moblie.management.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    ERROR_409(HttpStatus.CONFLICT, ""),
    ERROR_404(HttpStatus.NOT_FOUND, ""),
    ERROR_401(HttpStatus.UNAUTHORIZED, ""),
    ERROR_400(HttpStatus.BAD_REQUEST, "");

    private final HttpStatus httpStatus;
    private final String message;
}