package com.moblie.management.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class Response {

    private String message;
    private Map<String, String> errors;

    public Response(String message) {
        this.message = message;
    }

    public Response(String message, Map<String, String> errors) {
        this.message = message;
        this.errors = errors;
    }
}
