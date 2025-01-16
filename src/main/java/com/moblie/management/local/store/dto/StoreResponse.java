package com.moblie.management.local.store.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class StoreResponse {

    private String message;

    public StoreResponse(String message) {
        this.message = message;
    }

}
