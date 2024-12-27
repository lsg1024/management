package com.moblie.management.local.product.repository;

import com.moblie.management.local.product.domain.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {
    boolean existsByImageOriginName(String originName);
}
