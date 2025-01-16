package com.moblie.management.local.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class OrderDto {

    @Getter
    public static class productsDto {
        private List<order> productsDto;

        static class order {
            private String cart_id;
            private String order_id;
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
        private String productGoldType;
        private String productColor;
        private String productRequestNote;
        private int amount;
    }

    @Getter
    @NoArgsConstructor
    public static class productInfoDto {
        private String uniqueNumber;
        private String productId;
        private String productName;
        private String factory;
        private String productGoldType;
        private String productColor;
        private String productWeight;
        private String productRequestNote;
        private int amount;

        @QueryProjection
        public productInfoDto(String uniqueNumber, String productId, String productName, String factory, String productGoldType, String productColor, String productWeight, String productRequestNote, int amount) {
            this.uniqueNumber = uniqueNumber;
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

}
