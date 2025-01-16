package com.moblie.management.local.order.repository.order;

import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.order.model.Order;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory query;
    public OrderRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public PageCustom<Order> findByTrackingId(String trackingId) {


        return null;
    }
}
