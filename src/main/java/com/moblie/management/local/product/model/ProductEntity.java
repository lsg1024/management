package com.moblie.management.local.product.model;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.utils.BaseEntity;
import com.moblie.management.local.factory.model.FactoryEntity;
import com.moblie.management.local.member.model.MemberEntity;
import com.moblie.management.local.product.dto.ProductDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@SQLDelete(sql = "UPDATE PRODUCT set DELETED = true where PRODUCT_ID = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(unique = true, nullable = false)
    private String productName;
//    @Column(nullable = false)
//    private String productClassification;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factoryId", nullable = false)
    private FactoryEntity factory;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private MemberEntity member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classificationId", nullable = false)
    private ClassificationEntity classification;

    @OneToMany(
            mappedBy = "product",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<ProductImageEntity> productImageEntities = new ArrayList<>();

    @Builder
    private ProductEntity(String productName, FactoryEntity factory, MemberEntity member, ClassificationEntity classification, String productMaterial, String productColor, String productWeight, String productNote, String productBarcodeNumber) {
        this.productName = productName;
        this.factory = factory;
        this.member = member;
        this.classification = classification;
        this.productMaterial = productMaterial;
        this.productColor = productColor;
        this.productWeight = productWeight;
        this.productNote = productNote;
        this.productBarcodeNumber = productBarcodeNumber;
    }


    public static ProductEntity create(ProductDto.createProduct productDto, MemberEntity member, ClassificationEntity classification, FactoryEntity factory) {
        return ProductEntity.builder()
                .productName(productDto.getProductName())
                .factory(factory)
                .member(member)
                .classification(classification)
                .productMaterial(productDto.getGoldType())
                .productColor(productDto.getGoldColor())
                .productWeight(productDto.getModelWeight())
                .productNote(productDto.getModelNote())
                .productBarcodeNumber(productDto.getModelBarcode())
                .build();
    }

    public void productUpdate(ProductDto.productUpdate productUpdate, ClassificationEntity classification, FactoryEntity factory) {
        if (factory == null) {
            throw new CustomException(ErrorCode.ERROR_400, "유효하지 않은 공장 정보");
        }
        this.productName = productUpdate.getProductName();
        this.factory = factory;
        this.classification = classification;
//        this.productClassification = productUpdate.getModelClassification();
        this.productMaterial = productUpdate.getGoldType();
        this.productColor = productUpdate.getGoldColor();
        this.productWeight = productUpdate.getModelWeight();
        this.productNote = productUpdate.getModelNote();
    }

    public void addImage(ProductImageEntity image) {
        productImageEntities.add(image);
        image.setProduct(this);
    }

    public void validateProductAccess(String productId) {
        if (this.productId != Long.parseLong(productId)) {
            throw new CustomException(ErrorCode.ERROR_404, "잘못된 데이터 정보가 들어왔습니다.");
        }
    }

    public String generateImageFolderName() {
        return this.productName.replace(" ", "_");
    }

    public Long getProductId() {
        return productId;
    }

    public void delete() {
        this.deleted = true;
    }

}
