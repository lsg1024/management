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
@Table(name = "material")
@SQLDelete(sql = "UPDATE MATRIAL SET deleted = true WHERE material_id = ?")
@SQLRestriction("deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MaterialEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long materialId;

    @Column(unique = true, nullable = true)
    private String materialName;
    private boolean deleted = Boolean.FALSE;

    @OneToMany(mappedBy = "material")
    private List<ProductEntity> products = new ArrayList<>();

    @Builder
    public MaterialEntity(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialName() {
        return materialName;
    }
}
