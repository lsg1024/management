package com.moblie.management.local.product.repository;

import com.moblie.management.local.product.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductRepositoryCustom {
    ProductEntity findByProductName(String productName);
    boolean existsByProductName(String productName);
    boolean existsByProductBarcodeNumber(String barcodeNumber);
}
