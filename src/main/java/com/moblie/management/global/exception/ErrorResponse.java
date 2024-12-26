package com.moblie.management.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private Map<String, String> details;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
