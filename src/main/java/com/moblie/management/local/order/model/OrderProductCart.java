package com.moblie.management.local.order.model;

import com.moblie.management.global.utils.BaseEntity;
import com.moblie.management.local.order.dto.OrderDto;
import com.moblie.management.local.store.model.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.moblie.management.local.order.validation.OrderProductCartValidation.productsIsNotZeroOrNegative;
import static jakarta.persistence.CascadeType.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProductCart extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;
    private int totalProducts;

    @OneToOne(cascade = {PERSIST, REMOVE})
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(
            mappedBy = "orderProductCart",
            cascade = {PERSIST, MERGE})
    private List<OrderProduct> orderProducts = new ArrayList<>(); // 주문 상품들

    @Builder(access = AccessLevel.PROTECTED)
    private OrderProductCart(Store store, List<OrderProduct> orderProducts) {
        this.store = store;
        this.orderProducts = orderProducts;
    }

    public static OrderProductCart create(Store store) {
        return OrderProductCart.builder()
                .store(store)
                .build();
    }

    public static OrderProductCart update(OrderDto.createDto create) {
        return OrderProductCart.builder()
                .orderProducts(new ArrayList<>())
                .build();

    }

    public void addProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
    }

    public void updateTotalProducts(int products) {
        productsIsNotZeroOrNegative(products);
        this.totalProducts = products;
    }

    public Long getId() {
        return id;
    }

    public Store getStore() {
        return this.store;
    }

    public boolean canProcessOrder() {
        return totalProducts > 0 && store != null;
    }

    public List<OrderProduct> getOrderProducts() {
        return Collections.unmodifiableList(orderProducts);
    }

}
