package com.moblie.management.local.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CartDto {

    @Getter
    @NoArgsConstructor
    public static class create {
        @NotBlank(message = "추가하실 장바구니를 선택해주세요.")
        private String storeId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class cartResponse {
        private String cartId;
    }

    @Getter
    @NoArgsConstructor
    public static class addProduct {
        private String productGoldType;
        private String productColor;
        private String productRequestNote;
        private int amount;
    }

    @Getter
    @NoArgsConstructor
    public static class carts {
        private String cartId;
        private String storeName;

        @QueryProjection
        public carts(String cartId, String storeName) {
            this.cartId = cartId;
            this.storeName = storeName;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class productDetail {
        private String trackingId;
        private String productName;
        private String productClassification;
        private String productGoldType;
        private String productColor;
        private String productWeight;
        private String productNote;
        private int amount;
        private String factoryName;

        @QueryProjection
        public productDetail(String trackingId, String productName, String productClassification, String productGoldType, String productColor, String productWeight, String productNote, int amount, String factoryName) {
            this.trackingId = trackingId;
            this.productName = productName;
            this.productClassification = productClassification;
            this.productGoldType = productGoldType;
            this.productColor = productColor;
            this.productWeight = productWeight;
            this.productNote = productNote;
            this.amount = amount;
            this.factoryName = factoryName;
        }
    }

}
