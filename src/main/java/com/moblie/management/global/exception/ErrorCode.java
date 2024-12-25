package com.moblie.management.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    ERROR_409(HttpStatus.CONFLICT, "이미 존재하는 리소스입니다."),
    ERROR_404(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),
    ERROR_401(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    ERROR_400(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}