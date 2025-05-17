package com.moblie.management.local.order.model;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.utils.BaseEntity;
import com.moblie.management.local.store.model.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE orders SET DELETED = true WHERE order_id = ?")
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "tracking_id", unique = true)
    private String trackingId;

    @ManyToOne(fetch = FetchType.LAZY ,cascade = {PERSIST})
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = {PERSIST, REMOVE})
    private List<OrderProduct> orderProducts = new ArrayList<>(); //주문 상품

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // [CANCEL,WAIT,APPROVAL]

    private boolean deleted = Boolean.FALSE;

    @Builder(access = AccessLevel.PROTECTED)
    protected Order(String trackingId, Store store, List<OrderProduct> orderProducts, OrderStatus orderStatus) {
        this.trackingId = trackingId;
        this.store = store;
        this.orderProducts = orderProducts;
        this.orderStatus = orderStatus;
    }

    public static Order create(OrderProductCart cart) {
        if (!cart.canProcessOrder()) {
            throw new CustomException(ErrorCode.ERROR_404, "주문 수량이 1 이상이여야 합니다.");
        }
        return Order.builder()
                .trackingId(UUID.randomUUID().toString().toLowerCase())
                .store(cart.getStore())
                .orderProducts(new ArrayList<>())
                .orderStatus(OrderStatus.WAIT)
                .build();
    }

    public void addProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
    }

    public void updateStatusApproval() {
        this.orderStatus = OrderStatus.APPROVAL;
    }

    public void updateStatusCancel() {
        this.orderStatus = OrderStatus.CANCEL;
    }

}
