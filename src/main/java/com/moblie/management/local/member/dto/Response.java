package com.moblie.management.local.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class Response {

    private String message;
    private List<String> errors;

    public Response(String message) {
        this.message = message;
    }

    public Response(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
    }
}
