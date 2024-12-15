package com.moblie.management.product.domain;

import com.moblie.management.exception.CustomException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "product")
@SQLDelete(sql = "UPDATE PRODUCT set DELETED = true where PRODUCT_ID = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(unique = true, nullable = false)
    private String productName;
//    @Column(nullable = false)
//    private FactoryEntity factory;

    @Column(nullable = false)
    private String productClassification;
    @Column(nullable = false)
    private String productMaterial;
    @Column(nullable = false)
    private String productColor;
    @Column(nullable = false)
    private String productWeight;
    private String productNote;
    @Column(unique = true)
    private String productBarcodeNumber;
    private final boolean deleted = false;

    @Builder
    public ProductEntity(String productName, String productClassification, String productMaterial, String productColor, String productWeight, String productNote, String productBarcodeNumber) {
        this.productName = productName;
        this.productClassification = productClassification;
        this.productMaterial = productMaterial;
        this.productColor = productColor;
        this.productWeight = productWeight;
        this.productNote = productNote;
        this.productBarcodeNumber = productBarcodeNumber;
    }

}
