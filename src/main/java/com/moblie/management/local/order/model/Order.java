package com.moblie.management.local.order.model;

import com.moblie.management.global.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "tracking_id", unique = true)
    private String trackingId;

    //상품 주문하는 상점도 추가 매핑 필요

    @OneToMany(mappedBy = "order", cascade = {PERSIST, REMOVE})
    private List<OrderProduct> orderProducts = new ArrayList<>(); //주문 상품

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // [CANCEL,WAIT,DONE]

    @Builder(access = AccessLevel.PROTECTED)
    protected Order(String trackingId, List<OrderProduct> orderProducts, OrderStatus orderStatus) {
        this.trackingId = trackingId;
        this.orderProducts = orderProducts;
        this.orderStatus = orderStatus;
    }

    public static Order create() {
        return Order.builder()
                .trackingId(UUID.randomUUID().toString())
                .orderProducts(new ArrayList<>())
                .orderStatus(OrderStatus.WAIT)
                .build();
    }

    public void addProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
    }
}
