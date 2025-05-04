package com.moblie.management.local.order.service;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.redis.rock.DefaultRock;
import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.order.dto.CartDto;
import com.moblie.management.local.order.dto.OrderDto;
import com.moblie.management.local.order.model.OrderProduct;
import com.moblie.management.local.order.model.OrderProductCart;
import com.moblie.management.local.order.repository.order_product_cart.OrderProductCartRepository;
import com.moblie.management.local.order.repository.order_product.OrderProductRepository;
import com.moblie.management.local.product.model.ProductEntity;
import com.moblie.management.local.product.repository.ProductRepository;
import com.moblie.management.local.store.model.Store;
import com.moblie.management.local.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderProductCartService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderProductCartRepository orderProductCartRepository;

    @Transactional
    public String createNewCart(String userId, String storeId) {

        Store store = storeRepository.findById(Long.valueOf(storeId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404));

        // 오류가 아닌 카트 값을 반환 (기존 값은 데이터가 있겠지, 신규 값은 빈 카트 반환)
        if (orderProductCartRepository.existsByStoreAndCreatedBy(store, userId)) {
            return orderProductCartRepository.findByCreatedByAndId(userId, Long.valueOf(storeId))
                    .orElseThrow().getId().toString();
        }

        OrderProductCart orderProductCart = OrderProductCart.create(store);
        orderProductCartRepository.save(orderProductCart);

        return orderProductCart.getId().toString();
    }

    @Transactional
    public void addProductToCart(String cartId, String productId, CartDto.addProduct productDto) {
        OrderProductCart cart = orderProductCartRepository.findById(Long.valueOf(cartId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404, "장바구니 없음"));

        ProductEntity product = productRepository.findById(Long.valueOf(productId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404, "상품 없음"));

        try {
            OrderProduct orderProduct = OrderProduct.createOrders(productDto, product);
            orderProduct.setOrderProductCart(cart);
            cart.addProduct(orderProduct);
            cart.updateTotalProducts(cart.getOrderProducts().size());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.ERROR_400, e.getMessage());
        }
    }

    @Transactional
    public void updateProductToCart(String trackingId, OrderDto.updateDto updateDto) {

        OrderProduct product = orderProductRepository.findByOrderProductTrackingNumber(trackingId)
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404));

        product.updateProductInfo(updateDto);

    }

//    @Cacheable(value = "vLLC", key = "'cart:' + #userId", cacheManager = "redisCacheManager")
    public PageCustom<CartDto.carts> getCarts(String userId, Pageable pageable) {
        return orderProductCartRepository.findCartAll(userId, pageable);
    }

    public PageCustom<CartDto.productDetail> getCartProductDetail(String id, String cartTrackingId, Pageable pageable) {
        return orderProductCartRepository.findCartProductDetail(id, cartTrackingId, pageable);
    }

    @DefaultRock(key = "'cart:' + #trackingId")
    public void deleteProductToCart(String userId, String cartId, String trackingId) {

        Optional<OrderProductCart> original_cart = getOrderProductCart(userId, cartId);

        if (original_cart.isPresent()) {
            OrderProduct product = orderProductRepository.findByOrderProductTrackingNumber(trackingId)
                    .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404));

            orderProductRepository.delete(product);
            return;
        }
        throw new CustomException(ErrorCode.ERROR_400);
    }

    private Optional<OrderProductCart> getOrderProductCart(String userId, String cartId) {
        return orderProductCartRepository.findByCreatedByAndId(userId, Long.valueOf(cartId));
    }

}
