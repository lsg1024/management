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
    @Column(nullable = false)
    private String productWeight;
    private String productNote;
    @Column(unique = true)
    private String productBarcodeNumber;
    private boolean deleted = Boolean.FALSE;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factoryId", nullable = false)
    private FactoryEntity factory;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classificationId", nullable = false)
    private ClassificationEntity classification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materialId")
    private MaterialEntity material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colorId")
    private ColorEntity color;

    @OneToMany(
            mappedBy = "product",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<ProductImageEntity> productImageEntities = new ArrayList<>();

    @Builder
    private ProductEntity(String productName, FactoryEntity factory, MemberEntity member, ClassificationEntity classification, MaterialEntity material, ColorEntity color, String productWeight, String productNote, String productBarcodeNumber) {
        this.productName = productName;
        this.factory = factory;
        this.member = member;
        this.classification = classification;
        this.material = material;
        this.color = color;
        this.productWeight = productWeight;
        this.productNote = productNote;
        this.productBarcodeNumber = productBarcodeNumber;
    }


    public static ProductEntity create(ProductDto.productInfo productDto, MemberEntity member, ClassificationEntity classification, MaterialEntity material, ColorEntity color, FactoryEntity factory) {
        return ProductEntity.builder()
                .productName(productDto.getProductName())
                .factory(factory)
                .member(member)
                .classification(classification)
                .material(material)
                .color(color)
                .productWeight(productDto.getModelWeight())
                .productNote(productDto.getModelNote())
                .productBarcodeNumber(productDto.getModelBarcode())
                .build();
    }

    public ProductDto.productDetailInfo getProductDetailInfo() {
        return new ProductDto.productDetailInfo(
                this.productId.toString(),
                this.productName,
                null,
                this.classification.getClassificationName(),
                this.material.getMaterialName(),
                this.color.getColorName(),
                this.productWeight,
                this.productNote
        );
    }

    public void productUpdate(ProductDto.productUpdate productUpdate, ClassificationEntity classification, MaterialEntity material, ColorEntity color, FactoryEntity factory) {
        if (factory == null) {
            throw new CustomException(ErrorCode.ERROR_400, "유효하지 않은 공장 정보");
        }
        this.productName = productUpdate.getProductName();
        this.factory = factory;
        this.classification = classification;
        this.material = material;
        this.color = color;
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

}
