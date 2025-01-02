package com.moblie.management.local.product.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import static com.moblie.management.local.product.domain.QProductEntity.productEntity;
import static com.moblie.management.local.product.domain.QProductImageEntity.productImageEntity;

public class ProductImageRepositoryImpl implements ProductImageRepositoryCustom{
    private final JPAQueryFactory query;

    public ProductImageRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public boolean existByFirstPathImage(Long productId) {
        return query
                .selectOne()
                .from(productImageEntity)
                .join(productImageEntity.product, productEntity)
                .where(productImageEntity.firstImagePath.isNotNull()
                        .and(productEntity.productId.eq(productId)))
                .fetchFirst() != null;
    }

}
