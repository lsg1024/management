package com.moblie.management.local.product.repository;

import com.moblie.management.local.product.dto.ProductDto;
import com.moblie.management.global.utils.PageCustom;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    boolean existsByMemberIdAndProductId(String memberId, String productId);
    PageCustom<ProductDto.productDetailInfo> searchProduct(ProductDto.productCondition condition, Pageable pageable);
}
