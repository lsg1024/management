package com.moblie.management.local.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OrderDto {

    @Getter
    @NoArgsConstructor
    public static class productDto {
        private String orderId;
        private String storeName;
        private String createDate;
        private String modifiedDate;
        private String trackingId;
        private String status;

        @QueryProjection
        public productDto(String orderId, String storeName, String createDate, String modifiedDate, String trackingId, String status) {
            this.orderId = orderId;
            this.storeName = storeName;
            this.createDate = createDate;
            this.modifiedDate = modifiedDate;
            this.trackingId = trackingId;
            this.status = status;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class createDto {
        private String productId;
        private String productGoldType;
        private String productColor;
        private String productRequestNote;
        private int amount;
    }

    @Getter
    @NoArgsConstructor
    public static class updateDto {
        @NotEmpty(message = "필수 입력 값입니다.")
        private String productGoldType;
        @NotEmpty(message = "필수 입력 값입니다.")
        private String productColor;
        private String productRequestNote;
        @NotEmpty(message = "필수 입력 값입니다.")
        private int amount;

        @QueryProjection
        public updateDto(String productGoldType, String productColor, String productRequestNote, int amount) {
            this.productGoldType = productGoldType;
            this.productColor = productColor;
            this.productRequestNote = productRequestNote;
            this.amount = amount;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class productInfoDto {
        private String cartTrackingId;
        private String storeName;
        private String productId;
        private String productName;
        private String factory;
        private String productGoldType;
        private String productColor;
        private String productWeight;
        private String productRequestNote;
        private int amount;

        @QueryProjection
        public productInfoDto(String cartTrackingId, String storeName, String productId, String productName, String factory, String productGoldType, String productColor, String productWeight, String productRequestNote, int amount) {
            this.cartTrackingId = cartTrackingId;
            this.storeName = storeName;
            this.productId = productId;
            this.productName = productName;
            this.factory = factory;
            this.productGoldType = productGoldType;
            this.productColor = productColor;
            this.productWeight = productWeight;
            this.productRequestNote = productRequestNote;
            this.amount = amount;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class orderProducts {
        private String trackingId;
        private String productName;
        private String factory;
        private String classification;
        private String productGoldType;
        private String productColor;
        private String productWeight;
        private String productRequestNote;

        @QueryProjection
        public orderProducts(String trackingId, String productName, String factory, String classification, String productGoldType, String productColor, String productWeight, String productRequestNote) {
            this.trackingId = trackingId;
            this.productName = productName;
            this.factory = factory;
            this.classification = classification;
            this.productGoldType = productGoldType;
            this.productColor = productColor;
            this.productWeight = productWeight;
            this.productRequestNote = productRequestNote;
        }
    }

}
