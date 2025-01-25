package com.moblie.management.local.order.repository.order;

import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.order.dto.OrderDto;
import com.moblie.management.local.order.dto.QOrderDto_orderInfoDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.moblie.management.local.order.model.QOrder.order;
import static com.moblie.management.local.store.model.QStore.store;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory query;
    public OrderRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public PageCustom<OrderDto.orderInfoDto> findOrderInfo(OrderDto.orderCondition condition, Pageable pageable) {

        BooleanBuilder whereClause = new BooleanBuilder();
        if (StringUtils.hasText(condition.getStoreName())) {
            whereClause.and(store.storeName.eq(condition.getStoreName()));
        } else if (StringUtils.hasText(condition.getStatus())) {
            whereClause.and(order.orderStatus.stringValue().eq(condition.getStatus()));
        }

        List<OrderDto.orderInfoDto> content = query
                .select(new QOrderDto_orderInfoDto(
                        store.storeName,
                        order.lastModifiedDate.substring(0, 10),
                        order.orderStatus.stringValue()
                ))
                .from(order)
                .join(order.store, store)
                .where(whereClause)
                .orderBy(order.lastModifiedDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(order.count())
                .from(order)
                .where(whereClause);

        return new PageCustom<>(content, pageable, countQuery.fetchOne());
    }
}
