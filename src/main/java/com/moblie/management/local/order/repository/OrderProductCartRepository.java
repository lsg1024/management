package com.moblie.management.local.order.repository;

import com.moblie.management.local.order.model.OrderProductCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderProductCartRepository extends JpaRepository<OrderProductCart, Long>, OrderProductCartCustom {
    Optional<OrderProductCart> findByCreatedBy(String userId);
}
