package com.moblie.management.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@SQLDelete(sql = "UPDATE prouductEntity set deleted = true where product_id = false")
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
    private final boolean deleted = false;

}
