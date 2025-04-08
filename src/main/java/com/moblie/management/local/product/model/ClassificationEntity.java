package com.moblie.management.local.product.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "classification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClassificationEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classificationId;

    @Column(unique = true, nullable = false)
    private String classificationName;

    @OneToOne(mappedBy = "classification")
    private ProductEntity product;

    @Builder
    public ClassificationEntity(String classificationName) {
        this.classificationName = classificationName;
    }
}
