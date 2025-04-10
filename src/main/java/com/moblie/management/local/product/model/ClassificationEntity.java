package com.moblie.management.local.product.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClassificationEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classificationId;

    @Column(unique = true, nullable = false)
    private String classificationName;

    @OneToMany(mappedBy = "classification")
    private List<ProductEntity> products = new ArrayList<>();

    @Builder
    public ClassificationEntity(String classificationName) {
        this.classificationName = classificationName;
    }
}
