package com.moblie.management.local.order.repository;

import com.moblie.management.local.order.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
