package com.moblie.management.global.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@NoArgsConstructor
public class Response {

    private HttpStatus status;
    private String message;
    private List<String> errors;

    public Response(String message) {
        this.status = HttpStatus.OK;
        this.message = message;
    }

    public Response(String message, List<String> errors) {
        this.status = HttpStatus.OK;
        this.message = message;
        this.errors = errors;
    }

    public Response(HttpStatus status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }
}
