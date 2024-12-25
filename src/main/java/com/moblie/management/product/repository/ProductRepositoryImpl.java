package com.moblie.management.product.repository;

import com.moblie.management.product.dto.ProductDto;
import com.moblie.management.utils.PageCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.moblie.management.factory.domain.QFactoryEntity.factoryEntity;
import static com.moblie.management.product.domain.QProductEntity.*;

public class ProductRepositoryImpl implements ProductRepositoryCustom{

    private final JPAQueryFactory query;

    public ProductRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public boolean existsByMemberIdAndProductId(String memberId, String productId) {
        return query
                .selectOne()
                .from(productEntity)
                .where(
                        productEntity.productId.eq(Long.valueOf(productId))
                                .and(productEntity.member.userid.eq(Long.valueOf(memberId)))
                                .and(productEntity.deleted.eq(false)))
                .fetchFirst() != null;
    }

    /**
     * @param condition productName, factoryName, classification
     * @param pageable  16
     * @return productSearchResult
     */
    @Override
    public PageCustom<ProductDto.productSearchResult> searchProduct(ProductDto.productCondition condition, Pageable pageable) {

        BooleanBuilder whereClause = new BooleanBuilder();
        if (StringUtils.hasText(condition.getProductName())) {
            whereClause.and(productEntity.productName.eq(condition.getProductName()));
        }
        if (StringUtils.hasText(condition.getFactory())) {
            whereClause.and(factoryEntity.factoryName.eq(condition.getFactory()));
        }
        if (StringUtils.hasText(condition.getModelClassification())) {
            whereClause.and(productEntity.productClassification.eq(condition.getModelClassification()));
        }

        List<ProductDto.productSearchResult> content = query
                .select(Projections.constructor(ProductDto.productSearchResult.class,
                        productEntity.productName,
                        factoryEntity.factoryName,
                        productEntity.productClassification,
                        productEntity.productMaterial,
                        productEntity.productColor,
                        productEntity.productWeight,
                        productEntity.productNote
                ))
                .from(productEntity)
                .join(productEntity.factory, factoryEntity)
                .where(whereClause)
                .orderBy(productEntity.productName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(productEntity.count())
                .from(productEntity)
                .leftJoin(productEntity.factory, factoryEntity)
                .where(whereClause);

        // PageCustom 객체로 변환하여 반환
        return new PageCustom<>(content, pageable, countQuery.fetchOne());
    }

}
