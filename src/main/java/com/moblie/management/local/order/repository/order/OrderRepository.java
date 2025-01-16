package com.moblie.management.local.order.repository.order;

import com.moblie.management.local.order.model.Order;
import com.moblie.management.local.order.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByTrackingId(String trackingId);
}
