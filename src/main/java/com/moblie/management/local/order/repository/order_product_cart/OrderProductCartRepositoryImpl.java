package com.moblie.management.local.order.repository.order_product_cart;

import com.moblie.management.local.order.dto.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.moblie.management.local.order.model.QOrderProduct.orderProduct;
import static com.moblie.management.local.order.model.QOrderProductCart.orderProductCart;
import static com.moblie.management.local.product.model.QProductEntity.productEntity;
import static com.moblie.management.local.store.model.QStore.store;
import static com.moblie.management.local.product.model.QClassificationEntity.classificationEntity;

public class OrderProductCartRepositoryImpl implements OrderProductCartCustom{
    private final JPAQueryFactory query;
    public OrderProductCartRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<CartDto.carts> findCartAll(String userId) {
        return query
                .select(new QCartDto_carts(
                        orderProductCart.id.stringValue(),
                        store.storeName
                ))
                .from(orderProductCart)
                .join(orderProductCart.store, store)
                .where(orderProductCart.createdBy.eq(userId))
                .orderBy(orderProductCart.lastModifiedDate.asc())
                .fetch();
    }

    @Override
    public List<CartDto.productDetail> findUserCarts(String userId, String cartId) {
        return query
                .select(new QCartDto_productDetail(
                        orderProduct.orderProductTrackingNumber,
                        productEntity.productName,
                        classificationEntity.classificationName,
                        orderProduct.productGoldType,
                        orderProduct.productOrderColor,
                        productEntity.productWeight,
                        orderProduct.productOrderRequestNote,
                        orderProduct.amount,
                        productEntity.factory.factoryName

                ))
                .from(orderProductCart)
                .join(orderProductCart.orderProducts, orderProduct)
                .join(orderProduct.product, productEntity)
                .join(productEntity.classification, classificationEntity)
                .where(orderProductCart.id.eq(Long.parseLong(cartId)))
                .where(orderProductCart.createdBy.eq(userId))
                .orderBy(orderProduct.product.productName.asc())
                .fetch();

    }

//    @Override
//    public PageCustom<CartDto.productDetail> findCartProductDetailList(String userId, String cartId, Pageable pageable) {
//        List<CartDto.productDetail> content = query
//                .select(new QCartDto_productDetail(
//                        orderProduct.orderProductTrackingNumber,
//                        productEntity.productName,
//                        classificationEntity.classificationName,
//                        orderProduct.productGoldType,
//                        orderProduct.productOrderColor,
//                        productEntity.productWeight,
//                        orderProduct.productOrderRequestNote,
//                        orderProduct.amount,
//                        productEntity.factory.factoryName
//
//                ))
//                .from(orderProductCart)
//                .join(orderProductCart.orderProducts, orderProduct)
//                .join(orderProduct.product, productEntity)
//                .join(productEntity.classification, classificationEntity)
//                .where(orderProductCart.id.eq(Long.parseLong(cartId)))
//                .where(orderProductCart.createdBy.eq(userId))
//                .orderBy(orderProduct.product.productName.asc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        JPAQuery<Long> countQuery = query
//                .select(orderProduct.count())
//                .from(orderProduct);
//
//        return new PageCustom<>(content, pageable, countQuery.fetchOne());
//    }

    @Override
    public CartDto.productDetail findCartToTackingProductInfo(String userId, Long cartId, String trackingId) {

        return query
                .select(new QCartDto_productDetail(
                        orderProduct.orderProductTrackingNumber,
                        productEntity.productName,
                        classificationEntity.classificationName,
                        orderProduct.productGoldType,
                        orderProduct.productOrderColor,
                        productEntity.productWeight,
                        orderProduct.productOrderRequestNote,
                        orderProduct.amount,
                        productEntity.factory.factoryName
                ))
                .from(orderProductCart)
                .join(orderProductCart.orderProducts, orderProduct)
                .join(orderProduct.product, productEntity)
                .join(productEntity.classification, classificationEntity)
                .where(
                        orderProductCart.createdBy.eq(userId),
                        orderProductCart.id.eq(cartId),
                        orderProduct.orderProductTrackingNumber.eq(trackingId))
                .fetchOne();

    }


}
