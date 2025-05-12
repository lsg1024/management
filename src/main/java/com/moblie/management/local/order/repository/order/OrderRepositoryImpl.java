package com.moblie.management.local.order.repository.order;

import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.order.dto.OrderDto;
import com.moblie.management.local.order.dto.QOrderDto_productDto;
import com.moblie.management.local.order.model.Order;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.moblie.management.local.order.model.QOrder.order;
import static com.moblie.management.local.store.model.QStore.store;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory query;
    public OrderRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public PageCustom<Order> findTrackingId(String trackingId) {


        return null;
    }

    @Override
    public PageCustom<OrderDto.productDto> findByOrderProducts(String storeName, String startDate, String endDate, Pageable pageable) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        BooleanBuilder builder = new BooleanBuilder();

        //날짜 조회
        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            LocalDateTime startDateTime = LocalDateTime.parse(startDate + " 00:00:00", formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(endDate + " 23:59:59", formatter);

            builder.and(order.createDate.between(startDateTime.format(formatter), endDateTime.format(formatter)));
        }

        //상점 조회
        if (storeName != null && !storeName.isEmpty()) {
            builder.and(store.storeName.containsIgnoreCase(storeName));
        }

        List<OrderDto.productDto> content = query
                .select(new QOrderDto_productDto(
                        order.id.stringValue(),
                        store.storeName,
                        order.createDate.stringValue(),
                        order.lastModifiedDate.stringValue(),
                        order.trackingId,
                        order.orderStatus.stringValue()
                ))
                .from(order)
                .join(order.store, store)
                .where(builder)
                .orderBy(order.createDate.desc())
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(order.count())
                .from(order);

        return new PageCustom<>(content, pageable, countQuery.fetchOne());
    }

}
