package com.moblie.management.local.store.repository;

import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.store.dto.QStoreDto_storeSearchResponse;
import com.moblie.management.local.store.dto.StoreDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.moblie.management.local.store.model.QStore.store;

public class StoreRepositoryImpl implements StoreRepositoryCustom{

    private final JPAQueryFactory query;

    public StoreRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }


    @Override
    public PageCustom<StoreDto.storeSearchResponse> searchStories(StoreDto.storeCondition condition, Pageable pageable) {

        BooleanBuilder whereClause = new BooleanBuilder();
        if (StringUtils.hasText(condition.getStoreName())) {
            whereClause.and(store.storeName.eq(condition.getStoreName()));
        }

        List<StoreDto.storeSearchResponse> content = query
                .select(new QStoreDto_storeSearchResponse(
                        store.storeId.stringValue(),
                        store.storeName,
                        store.storePhoneNumber))
                .from(store)
                .where(whereClause)
                .orderBy(store.storeName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(store.count())
                .from(store)
                .where(whereClause);

        return new PageCustom<>(content, pageable, countQuery.fetchOne());
    }
}
