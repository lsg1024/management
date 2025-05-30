package com.moblie.management.local.product.repository;

import com.moblie.management.local.product.dto.ProductDto;
import com.moblie.management.global.utils.PageCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.moblie.management.local.factory.model.QFactoryEntity.factoryEntity;
import static com.moblie.management.local.product.model.QProductEntity.productEntity;
import static com.moblie.management.local.product.model.QMaterialEntity.materialEntity;
import static com.moblie.management.local.product.model.QColorEntity.colorEntity;
import static com.moblie.management.local.product.model.QClassificationEntity.classificationEntity;

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
    public PageCustom<ProductDto.productDetailInfo> searchProduct(ProductDto.productCondition condition, Pageable pageable) {

        BooleanBuilder whereClause = new BooleanBuilder();
        if (StringUtils.hasText(condition.getProductName())) {
            whereClause.and(productEntity.productName.containsIgnoreCase(condition.getProductName()));
        }
        if (StringUtils.hasText(condition.getFactory())) {
            whereClause.and(factoryEntity.factoryName.eq(condition.getFactory()));
        }
        if (StringUtils.hasText(condition.getModelClassification())) {
            whereClause.and(classificationEntity.classificationName.eq(condition.getModelClassification()));
        }

        List<ProductDto.productDetailInfo> content = query
                .select(Projections.constructor(ProductDto.productDetailInfo.class,
                        productEntity.productId.stringValue(),
                        productEntity.productName,
                        factoryEntity.factoryName,
                        classificationEntity.classificationName,
                        materialEntity.materialName,
                        colorEntity.colorName,
                        productEntity.productWeight,
                        productEntity.productNote
                ))
                .from(productEntity)
                .join(productEntity.factory, factoryEntity)
                .join(productEntity.classification, classificationEntity)
                    .on(classificationEntity.deleted.eq(true).or(classificationEntity.deleted.eq(false)))
                .join(productEntity.material, materialEntity)
                    .on(materialEntity.deleted.eq(true).or(materialEntity.deleted.eq(false)))
                .join(productEntity.color, colorEntity)
                    .on(colorEntity.deleted.eq(true).or(colorEntity.deleted.eq(false)))
                .where(whereClause)
                .orderBy(productEntity.productName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(productEntity.count())
                .from(productEntity)
                .leftJoin(productEntity.factory, factoryEntity)
                .leftJoin(productEntity.classification, classificationEntity)
                    .on(classificationEntity.deleted.eq(true)
                            .or(classificationEntity.deleted.eq(false)))
                .where(whereClause);

        // PageCustom 객체로 변환하여 반환
        return new PageCustom<>(content, pageable, countQuery.fetchOne());
    }

}
