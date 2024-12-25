package com.moblie.management.product.repository;

import com.moblie.management.product.dto.ProductDto;
import com.moblie.management.utils.PageCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    boolean existsByMemberIdAndProductId(String memberId, String productId);
    PageCustom<ProductDto.productSearchResult> searchProduct(ProductDto.productCondition condition, Pageable pageable);
}
