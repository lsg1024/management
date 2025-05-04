package com.moblie.management.local.product.repository;

import com.moblie.management.local.product.model.ColorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<ColorEntity, Long> {
    ColorEntity findByColorName(String colorName);
    boolean existsByColorName(String colorName);
}
