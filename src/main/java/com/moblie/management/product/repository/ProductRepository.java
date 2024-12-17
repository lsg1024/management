package com.moblie.management.product.repository;

import com.moblie.management.product.domain.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    boolean existsByProductName(String productName);
    boolean existsByProductNameOrProductBarcodeNumber(String productName, String barcodeNumber);
}
