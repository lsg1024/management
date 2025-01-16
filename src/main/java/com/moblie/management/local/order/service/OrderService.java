package com.moblie.management.local.order.service;

import com.moblie.management.local.order.model.Order;
import com.moblie.management.local.order.model.OrderProduct;
import com.moblie.management.local.order.model.OrderProductCart;
import com.moblie.management.local.order.repository.order_product_cart.OrderProductCartRepository;
import com.moblie.management.local.order.repository.order_product.OrderProductRepository;
import com.moblie.management.local.order.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderProductCartRepository orderProductCartRepository;

    public void createOrder(String userId) {

        Optional<OrderProductCart> cart = orderProductCartRepository.findByCreatedBy(userId);
        Order order = Order.create();

        if (cart.isPresent()) {
            List<OrderProduct> products = orderProductRepository.findByOrderProductCart(cart.get());
            for (OrderProduct product : products) {
                product.deletedCart();
                product.setOrder(order);
                order.addProduct(product);
            }
            orderRepository.save(order);
            orderProductCartRepository.delete(cart.get());
        }
    }

    public void orderApproval(String trackingId) {
        Order order = orderRepository.findByTrackingId(trackingId);
    }

}
