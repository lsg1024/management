package com.moblie.management.product.repository;

import com.moblie.management.product.domain.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    boolean existsByProductNameAndProductBarcodeNumber(String productName, String barcodeNumber);
}
