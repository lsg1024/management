package com.moblie.management.product.dto;

import com.moblie.management.product.domain.ProductEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class ProductDto {

    @Getter @Setter
    @NoArgsConstructor
    public static class createProduct {
        private String modelName;
        private String factory;
        private String modelClassification;
        private String goldType;
        private String goldColor;
        private String modelWeight;
        private String modelNote;
        private String modelBarcode;

        public createProduct(String modelName, String factory, String modelClassification, String goldType, String goldColor, String modelWeight, String modelNote) {
            this.modelName = modelName;
            this.factory = factory;
            this.modelClassification = modelClassification;
            this.goldType = goldType;
            this.goldColor = goldColor;
            this.modelWeight = modelWeight;
            this.modelNote = modelNote;
        }

        public ProductEntity toEntity() {
            return ProductEntity.builder()
                    .productName(modelName)
                    .productBarcodeNumber(modelBarcode)
                    .productClassification(modelClassification)
                    .productMaterial(goldType)
                    .productColor(goldColor)
                    .productWeight(modelWeight)
                    .productNote(modelNote)
                    .build();
        }
    }

    public static class productsInfo {
        public List<createProduct> products;
    }

}
