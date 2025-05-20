package com.moblie.management.local.order.repository.order_product;

import com.moblie.management.local.order.dto.OrderDto;
import com.moblie.management.local.order.dto.QOrderDto_orderProducts;
import com.moblie.management.local.order.model.OrderProduct;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.moblie.management.local.factory.model.QFactoryEntity.factoryEntity;
import static com.moblie.management.local.order.model.QOrderProduct.orderProduct;
import static com.moblie.management.local.product.model.QClassificationEntity.classificationEntity;
import static com.moblie.management.local.product.model.QProductEntity.productEntity;

@Repository
public class OrderProductImpl implements OrderProductCustom{

    private final JPAQueryFactory query;

    public OrderProductImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Optional<OrderDto.orderProducts> findByOptNumber(String orderProductTrackingNumber) {
        OrderDto.orderProducts productInfo = query
                .select(new QOrderDto_orderProducts(
                        orderProduct.orderProductTrackingNumber,
                        productEntity.productName,
                        factoryEntity.factoryName,
                        classificationEntity.classificationName,
                        orderProduct.productGoldType,
                        orderProduct.productOrderColor,
                        productEntity.productWeight,
                        orderProduct.amount,
                        orderProduct.productOrderRequestNote
                ))
                .from(orderProduct)
                .join(orderProduct.product, productEntity)
                .join(productEntity.factory, factoryEntity)
                .join(productEntity.classification, classificationEntity)
                .where(orderProduct.orderProductTrackingNumber.eq(orderProductTrackingNumber)
                        .and(orderProduct.deleted.eq(false)))
                .fetchOne();

        return Optional.ofNullable(productInfo);
    }

    @Override
    public void softDeletedByOrderTrackingId(String tracking_id) {
        query
            .update(orderProduct)
            .set(orderProduct.deleted, true)
            .where(orderProduct.order.trackingId.eq(tracking_id))
            .execute();
    }
}
