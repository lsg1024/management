package com.moblie.management.local.order.repository;

import com.moblie.management.local.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
