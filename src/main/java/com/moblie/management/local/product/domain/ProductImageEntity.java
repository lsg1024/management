package com.moblie.management.local.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImageEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;
    private String imageName;
    private String imageOriginName;
    private String imagePath;
    private String firstImagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private ProductEntity product;

    @Builder
    public ProductImageEntity(String imageName, String imageOriginName, String imagePath, String firstImagePath) {
        this.imageName = imageName;
        this.imageOriginName = imageOriginName;
        this.imagePath = imagePath;
        this.firstImagePath = firstImagePath;
    }

    public void addProductImages(ProductImageEntity productImage, ProductEntity product) {
        if (this.imageOriginName.equals(productImage.imageOriginName)) {
            this.imageName = productImage.imageName;
        } else {
            this.product = product;
        }
    }

}
