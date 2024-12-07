package com.moblie.management.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USERNAME_DUPLICATED(HttpStatus.CONFLICT, ""),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, ""),
    EMAIL_DUPLICATE_FAILED(HttpStatus.BAD_REQUEST, "이미 가입된 사용자 이메일 입니다."),
    REQUEST_FAILED(HttpStatus.BAD_REQUEST, "");

    private final HttpStatus httpStatus;
    private final String message;
}