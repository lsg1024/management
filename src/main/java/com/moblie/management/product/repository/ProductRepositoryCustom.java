package com.moblie.management.product.repository;

import com.moblie.management.product.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    boolean existsByMemberIdAndProductId(String memberId, String productId);
    Page<ProductDto.productSearchResult> searchProduct(ProductDto.productCondition condition, Pageable pageable);
}
