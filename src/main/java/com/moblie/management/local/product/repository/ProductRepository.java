package com.moblie.management.local.product.repository;

import com.moblie.management.local.product.domain.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductRepositoryCustom {
    boolean existsByProductName(String productName);
    boolean existsByProductNameOrProductBarcodeNumber(String productName, String barcodeNumber);
}