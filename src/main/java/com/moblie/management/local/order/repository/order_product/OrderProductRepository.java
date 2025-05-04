package com.moblie.management.local.order.repository.order_product;

import com.moblie.management.local.order.dto.CartDto;
import com.moblie.management.local.order.dto.OrderDto;
import com.moblie.management.local.order.model.OrderProduct;
import com.moblie.management.local.order.model.OrderProductCart;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    Optional<OrderProduct> findByOrderProductTrackingNumber(String productTrackingNumber);
    List<OrderProduct> findByOrderProductCart(OrderProductCart cart);


}
