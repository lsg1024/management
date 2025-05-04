package com.moblie.management.local.product.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classification")
@SQLDelete(sql = "UPDATE CLASSIFICATION SET deleted = true WHERE classification_id = ?")
@SQLRestriction("deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClassificationEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classificationId;

    @Column(unique = true, nullable = false)
    private String classificationName;

    private boolean deleted = Boolean.FALSE;

    @OneToMany(mappedBy = "classification")
    private List<ProductEntity> products = new ArrayList<>();

    @Builder
    public ClassificationEntity(String classificationName) {
        this.classificationName = classificationName;
    }

    public String getClassificationName() {
        return classificationName;
    }

}
