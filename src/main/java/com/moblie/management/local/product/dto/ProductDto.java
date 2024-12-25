package com.moblie.management.local.product.dto;

import com.moblie.management.local.factory.domain.FactoryEntity;
import com.moblie.management.local.product.domain.ProductEntity;
import com.moblie.management.local.member.domain.MemberEntity;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class ProductDto {

    @Getter @Setter
    @NoArgsConstructor
    public static class createProduct {

        @NotEmpty(message = "제품 이름을 입력해주세요.")
        private String productName;
        private String factory;
        private String modelClassification;
        private String goldType;
        private String goldColor;
        private String modelWeight;
        private String modelNote;
        private String modelBarcode;

        public createProduct(String productName, String factory, String modelClassification, String goldType, String goldColor, String modelWeight, String modelNote) {
            this.productName = productName;
            this.factory = factory;
            this.modelClassification = modelClassification;
            this.goldType = goldType;
            this.goldColor = goldColor;
            this.modelWeight = modelWeight;
            this.modelNote = modelNote;
        }

        public ProductEntity toEntity(FactoryEntity factory, MemberEntity member) {
            return ProductEntity.builder()
                    .productName(productName)
                    .productBarcodeNumber(modelBarcode)
                    .productClassification(modelClassification)
                    .productMaterial(goldType)
                    .productColor(goldColor)
                    .productWeight(modelWeight)
                    .productNote(modelNote)
                    .factory(factory)
                    .member(member)
                    .build();
        }
    }

    public static class productsInfo {
        @Valid @NotNull
        public List<createProduct> products;
    }

    @Getter
    @AllArgsConstructor
    public static class productUpdate {

        @NotBlank(message = "상품 이름을 입력해주세요.")
        private String productName;
        @NotBlank(message = "공장을 선택해주세요.")
        private String factory;
        @NotBlank(message = "상품 유형을 선택해주세요.")
        private String modelClassification;
        @NotBlank(message = "상품 재질을 선택해주세요.")
        private String goldType;
        @NotBlank(message = "상품 색상을 선택해주세요.")
        private String goldColor;
        @NotBlank(message = "상품 무게를 입력해주세요.")
        private String modelWeight;
        private String modelNote;

    }

    @Getter
    @AllArgsConstructor
    public static class productCondition {
        private String productName;
        private String factory;
        private String modelClassification;

    }

    @Getter @Setter
    @NoArgsConstructor
    public static class productSearchResult {
        private String modelName;
        private String factory;
        private String modelClassification;
        private String goldType;
        private String goldColor;
        private String modelWeight;
        private String modelNote;

        @QueryProjection
        public productSearchResult(String modelName, String factory, String modelClassification, String goldType, String goldColor, String modelWeight, String modelNote) {
            this.modelName = modelName;
            this.factory = factory;
            this.modelClassification = modelClassification;
            this.goldType = goldType;
            this.goldColor = goldColor;
            this.modelWeight = modelWeight;
            this.modelNote = modelNote;
        }
    }

}
