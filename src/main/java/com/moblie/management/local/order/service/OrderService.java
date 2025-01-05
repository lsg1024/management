package com.moblie.management.local.order.service;

import com.moblie.management.local.order.model.Order;
import com.moblie.management.local.order.model.OrderProductCart;
import com.moblie.management.local.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void createOrderRequest(OrderProductCart cart) {
        Order order = Order.create(cart);
        orderRepository.save(order);
    }

}
