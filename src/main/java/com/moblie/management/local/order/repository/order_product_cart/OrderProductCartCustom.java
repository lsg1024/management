package com.moblie.management.local.order.repository.order_product_cart;

import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.order.dto.CartDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderProductCartCustom {
    List<CartDto.carts> findCartAll(String userId);
    List<CartDto.productDetail> findUserCarts(String userId, String cartId);
//    PageCustom<CartDto.productDetail> findCartProductDetailList(String userId, String cartId, Pageable pageable);
    CartDto.productDetail findCartToTackingProductInfo(String userId, Long cartId, String trackingId);
}
