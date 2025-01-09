package com.moblie.management.local.factory.repository;

import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.factory.dto.FactoryDto;
import com.moblie.management.local.factory.dto.QFactoryDto_factoriesResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.moblie.management.local.factory.model.QFactoryEntity.factoryEntity;

public class FactoryRepositoryImpl implements FactoryRepositoryCustom{

    private final JPAQueryFactory query;

    public FactoryRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public PageCustom<FactoryDto.factoriesResponse> searchFactories(FactoryDto.factoryCondition condition, Pageable pageable) {

        BooleanBuilder whereClause = new BooleanBuilder();
        if (StringUtils.hasText(condition.getFactoryName())) {
            whereClause.and(factoryEntity.factoryName.eq(condition.getFactoryName()));
        }

        List<FactoryDto.factoriesResponse> content = query
                .select(new QFactoryDto_factoriesResponse(
                        factoryEntity.factoryId,
                        factoryEntity.factoryName))
                .from(factoryEntity)
                .where(
                        whereClause
                                .and(factoryEntity.deleted.eq(false)))
                .orderBy(
                        factoryEntity.factoryName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(factoryEntity.count())
                .from(factoryEntity)
                .where(
                        whereClause
                                .and(factoryEntity.deleted.eq(false)));

        return new PageCustom<>(content, pageable, countQuery.fetchOne());
    }
}
