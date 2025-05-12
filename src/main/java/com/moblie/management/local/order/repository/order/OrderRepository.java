package com.moblie.management.local.order.repository.order;

import com.moblie.management.local.order.model.Order;
import com.moblie.management.local.order.model.OrderProduct;
import com.moblie.management.local.order.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    @Query("select o from Order o where o.trackingId = ?1")
    Optional<Order> findByTrackingId(String trackingId);
}
