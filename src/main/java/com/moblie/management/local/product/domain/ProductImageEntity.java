package com.moblie.management.local.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
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

    private ProductImageEntity(String imageName, String imageOriginName, String imagePath, String firstImagePath) {
        this.imageName = imageName;
        this.imageOriginName = imageOriginName;
        this.imagePath = imagePath;
        this.firstImagePath = firstImagePath;
    }

    public static ProductImageEntity create(String newFileName, String imageOriginalName, String path, String firstImagePath) {
        return new ProductImageEntity(
                newFileName,
                imageOriginalName,
                path + "/" + newFileName,
                firstImagePath);
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

}
