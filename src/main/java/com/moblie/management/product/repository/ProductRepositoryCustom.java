package com.moblie.management.product.repository;

import com.moblie.management.member.domain.MemberEntity;
import com.moblie.management.product.domain.ProductEntity;
import com.moblie.management.product.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepositoryCustom {

    Page<ProductDto.productSearchResult> searchProduct(ProductDto.productCondition condition, Pageable pageable);
}
