package com.moblie.management.local.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderResponse {
    private String message;

    public OrderResponse(String message) {
        this.message = message;
    }
}
