package com.moblie.management.local.store.dto;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public class StoreDto {

    @Getter
    @NoArgsConstructor
    public static class commonDto {
        @NotBlank(message = "가게 이름을 입력해주세요.")
        private String storeName;
        private String storePhone;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class storeCondition {
        private String storeName;
    }

    @Getter
    @NoArgsConstructor
    public static class storeSearchResponse {
        private String storeId;
        private String storeName;
        private String storePhone;

        @QueryProjection
        public storeSearchResponse(String storeId, String storeName, String storePhone) {
            this.storeId = storeId;
            this.storeName = storeName;
            this.storePhone = storePhone;
        }
    }

}
