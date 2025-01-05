package com.moblie.management.local.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class OrderDto {

    @Getter
    public static class ProductsDto {
        private final List<ProductDto> productsDto = new ArrayList<>();
    }

    @Getter
    @NoArgsConstructor
    public static class ProductDto {
        private String productId;
        private String productMaterial;
        private String productColor;
        private String productRequestNote;
        private int amount;

    }

}
