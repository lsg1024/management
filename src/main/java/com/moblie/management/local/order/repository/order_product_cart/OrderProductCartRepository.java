package com.moblie.management.local.order.repository.order_product_cart;

import com.moblie.management.local.order.model.OrderProductCart;
import com.moblie.management.local.store.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface OrderProductCartRepository extends JpaRepository<OrderProductCart, Long>, OrderProductCartCustom {
    Optional<OrderProductCart> findByCreatedBy(String userId);
    Optional<OrderProductCart> findByCreatedByAndId(String userId, Long cartId);
    boolean existsByStoreAndCreatedBy(Store store, String createBy);
    Optional<OrderProductCart> findByStoreAndCreatedBy(Store store, String createBy);


}
