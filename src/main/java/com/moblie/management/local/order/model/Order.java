package com.moblie.management.local.order.model;

import com.moblie.management.global.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static jakarta.persistence.CascadeType.MERGE;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(unique = true)
    private UUID trackingId;

    @OneToOne(cascade = MERGE)
    private OrderProductCart orderProductCart; // 상품 주문장

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // [CANCEL,WAIT,DONE]

    protected Order(UUID trackingId, OrderProductCart orderProductCart, OrderStatus orderStatus) {
        this.trackingId = trackingId;
        this.orderProductCart = orderProductCart;
        this.orderStatus = orderStatus;
    }

    public static Order create(OrderProductCart cart) {
        return new Order(
                UUID.randomUUID(),
                cart,
                OrderStatus.WAIT
        );
    }
}
