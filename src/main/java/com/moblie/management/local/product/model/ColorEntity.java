package com.moblie.management.local.product.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "color")
@SQLDelete(sql = "UPDATE COLER SET deleted = true where color_id = ?")
@SQLRestriction("deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ColorEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long colorId;

    @Column(unique = true, nullable = false)
    protected String colorName;

    private boolean deleted = Boolean.FALSE;

    @OneToMany(mappedBy = "color")
    private List<ProductEntity> products = new ArrayList<>();

    @Builder
    public ColorEntity(String colorName) {
        this.colorName = colorName;
    }

    public String getColorName() {
        return colorName;
    }
}