package com.moblie.management.product.domain;

import com.moblie.management.factory.domain.FactoryEntity;
import com.moblie.management.product.dto.ProductDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factoryId")
    private FactoryEntity factory;

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
    private boolean deleted = false;

    @Builder
    public ProductEntity(String productName, FactoryEntity factory, String productClassification, String productMaterial, String productColor, String productWeight, String productNote, String productBarcodeNumber) {
        this.productName = productName;
        this.factory = factory;
        this.productClassification = productClassification;
        this.productMaterial = productMaterial;
        this.productColor = productColor;
        this.productWeight = productWeight;
        this.productNote = productNote;
        this.productBarcodeNumber = productBarcodeNumber;
    }

    public void productUpdate(ProductDto.productUpdate productUpdate) {
        this.productName = productUpdate.getProductName();
//        this.factory = productUpdate.getFactory();
        this.productClassification = productUpdate.getModelClassification();
        this.productMaterial = productUpdate.getGoldType();
        this.productColor = productUpdate.getGoldColor();
        this.productWeight = productUpdate.getModelWeight();
        this.productNote = productUpdate.getModelNote();
    }

    public void delete() {
        this.deleted = true;
    }

}
