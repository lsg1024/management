package com.moblie.management.local.order.model;

import com.moblie.management.global.utils.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProductCart extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @OneToMany(mappedBy = "orderProductCart")
    private List<OrderProduct> orderProduct; // 주문 상품들

    @Builder
    public OrderProductCart(List<OrderProduct> orderProduct) {
        this.orderProduct = orderProduct;
    }


    public void addProduct(OrderProduct orderProduct) {
        this.orderProduct.add(orderProduct);
        orderProduct.setOrderProductCart(this);
    }
}
