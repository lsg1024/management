package com.moblie.management.local.order.model;

import com.moblie.management.local.order.dto.OrderDto;
import com.moblie.management.local.product.model.ProductEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long id;
    @Column(nullable = false)
    private String productOrderMaterial;
    @Column(nullable = false)
    private String productOrderColor;
    private String productOrderRequestNote;
    private int amount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_item_id", nullable = false)
    private OrderProductCart orderProductCart;

    public OrderProduct(String productOrderMaterial, String productOrderColor, String productOrderRequestNote, int new_amount, ProductEntity product) {
        this.productOrderMaterial = productOrderMaterial;
        this.productOrderColor = productOrderColor;
        this.productOrderRequestNote = productOrderRequestNote;
        this.amount = new_amount;
        this.product = product;
    }

    public static OrderProduct createOrders(OrderDto.ProductDto productDto, ProductEntity product) {

        return new OrderProduct(
                productDto.getProductMaterial(),
                productDto.getProductColor(),
                productDto.getProductRequestNote(),
                addCount(productDto.getAmount()),
                product
        );
    }

    private static int addCount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
        }
        return amount;
    }
    public void updateCount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
        }
        this.amount += amount;
    }

    public void setOrderProductCart(OrderProductCart orderProductCart) {
        this.orderProductCart = orderProductCart;
    }

}
