package com.moblie.management.local.product.repository;

import com.moblie.management.local.product.model.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long>, ProductImageRepositoryCustom {
    boolean existsByImageOriginName(String originName);

    void deleteByImageName(String imageId);
}
