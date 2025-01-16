package com.moblie.management.local.factory.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FactoryResponse {

    private String message;
    private List<String> errors;

    public FactoryResponse(String message) {
        this.message = message;
    }

    public FactoryResponse(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
    }
}
