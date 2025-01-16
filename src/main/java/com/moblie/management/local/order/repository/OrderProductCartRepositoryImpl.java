package com.moblie.management.local.order.repository;

import com.moblie.management.local.order.dto.OrderDto;
import com.moblie.management.local.order.dto.QOrderDto_productInfoDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.moblie.management.local.order.model.QOrderProduct.orderProduct;
import static com.moblie.management.local.order.model.QOrderProductCart.orderProductCart;
import static com.moblie.management.local.product.model.QProductEntity.productEntity;

public class OrderProductCartRepositoryImpl implements OrderProductCartCustom{
    private final JPAQueryFactory query;
    public OrderProductCartRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<OrderDto.productInfoDto> findAll(String userId) {
        return query
                .select(new QOrderDto_productInfoDto(
                        orderProduct.uniqueNumber,
                        productEntity.productId.stringValue(),
                        productEntity.productName,
                        productEntity.factory.factoryName,
                        orderProduct.productGoldType,
                        orderProduct.productOrderColor,
                        productEntity.productWeight,
                        orderProduct.productOrderRequestNote,
                        orderProduct.amount
                ))
                .from(orderProductCart)
                .join(orderProductCart.orderProducts, orderProduct)
                .join(orderProduct.product, productEntity)
                .where(orderProductCart.createdBy.eq(userId))
                .fetch();
    }
}
