package com.moblie.management.local.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProductResponse {

    private String message;
    private List<String> errorProducts;

    public ProductResponse(String message) {
        this.message = message;
    }

    public ProductResponse(String message, List<String> errorProducts) {
        this.message = message;
        this.errorProducts = errorProducts;
    }

}
