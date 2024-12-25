package com.moblie.management.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class Response {

    private String message;
    private final Map<String, String> errors = new HashMap<>();

    public Response(String message) {
        this.message = message;
    }
}
