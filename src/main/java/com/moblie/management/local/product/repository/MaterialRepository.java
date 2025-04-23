package com.moblie.management.local.product.repository;

import com.moblie.management.local.product.model.MaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<MaterialEntity, Long> {

    MaterialEntity findByMaterialName(String materialName);
    boolean existsByMaterialName(String materialName);
}
