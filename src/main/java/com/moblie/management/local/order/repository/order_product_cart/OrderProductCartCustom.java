package com.moblie.management.local.order.repository.order_product_cart;

import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.order.dto.CartDto;
import org.springframework.data.domain.Pageable;

public interface OrderProductCartCustom {
    PageCustom<CartDto.carts> findCartAll(String userId, Pageable pageable);
    PageCustom<CartDto.productDetail> findCartProductDetailList(String userId, String cartId, Pageable pageable);
    CartDto.productDetail findCartToTackingProductInfo(String userId, Long cartId, String trackingId);
}
