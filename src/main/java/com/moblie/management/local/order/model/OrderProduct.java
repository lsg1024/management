package com.moblie.management.local.order.model;

import com.moblie.management.local.order.dto.CartDto;
import com.moblie.management.local.order.dto.OrderDto;
import com.moblie.management.local.product.model.ProductEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.util.UUID;

import static com.moblie.management.local.order.validation.OrderProductValidation.amountIsNotZeroOrNegative;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE ORDER_PRODUCT SET DELETED = true WHERE id = ?")
public class OrderProduct {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long id;
    private String orderProductTrackingNumber;
    @Column(nullable = false)
    private String productGoldType;
    @Column(nullable = false)
    private String productOrderColor;
    private String productOrderRequestNote;
    private int amount;

    private boolean deleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_item_id")
    private OrderProductCart orderProductCart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "tracking_id")
    private Order order;

    public OrderProduct(String productGoldType, String productOrderColor, String productOrderRequestNote, int new_amount, ProductEntity product) {
        this.orderProductTrackingNumber = String.valueOf(UUID.randomUUID());
        this.productGoldType = productGoldType;
        this.productOrderColor = productOrderColor;
        this.productOrderRequestNote = productOrderRequestNote;
        this.amount = new_amount;
        this.product = product;
    }

    public static OrderProduct createOrders(CartDto.addProduct createDto, ProductEntity product) {

        return new OrderProduct(
                createDto.getProductGoldType(),
                createDto.getProductColor(),
                createDto.getProductRequestNote(),
                addCount(createDto.getAmount()),
                product
        );
    }

    private static int addCount(int amount) {
        amountIsNotZeroOrNegative(amount);
        return amount;
    }
    public void updateCount(int amount) {
        amountIsNotZeroOrNegative(amount);
        this.amount = amount;
    }

    public void updateProductInfo(OrderDto.updateDto updateDto) {
        this.productGoldType = updateDto.getProductGoldType();
        this.productOrderColor = updateDto.getProductColor();
        this.productOrderRequestNote  = updateDto.getProductRequestNote();
        updateCount(updateDto.getAmount());
    }

    public void setOrderProductCart(OrderProductCart orderProductCart) {
        this.orderProductCart = orderProductCart;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void deletedCart() {
        this.orderProductCart = null;
    }

}
