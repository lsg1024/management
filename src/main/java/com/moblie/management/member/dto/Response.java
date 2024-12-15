package com.moblie.management.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class Response {

    private String message;
    private Map<String, String> errors;
    private List<String> errorProducts;

    public Response(String message) {
        this.message = message;
    }

    public Response(String message, List<String> errorProducts) {
        this.message = message;
        this.errorProducts = errorProducts;
    }

    public Response(String message, Map<String, String> errors) {
        this.message = message;
        this.errors = errors;
    }
}
