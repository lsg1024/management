package com.moblie.management.local.order.service;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.redis.rock.DefaultRock;
import com.moblie.management.local.order.dto.OrderDto;
import com.moblie.management.local.order.model.OrderProduct;
import com.moblie.management.local.order.model.OrderProductCart;
import com.moblie.management.local.order.repository.order_product_cart.OrderProductCartRepository;
import com.moblie.management.local.order.repository.order_product.OrderProductRepository;
import com.moblie.management.local.product.model.ProductEntity;
import com.moblie.management.local.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProductCartService {

    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderProductCartRepository orderProductCartRepository;

    @Transactional
    @CacheEvict(value = "vLLC", key = "'cart:' + #userId", cacheManager = "redisCacheManager")
    public void createProductCart(String userId, OrderDto.createDto productDto) {

        Optional<OrderProductCart> original_cart = getOrderProductCart(userId);

        if (original_cart.isPresent()) {
            addProductToCart(productDto, original_cart.get());
            return;
        }
        OrderProductCart cart = OrderProductCart.create();
        addProductToCart(productDto, cart);

    }

    @Transactional
    @CacheEvict(value = "vLLC", key = "'cart:' + #userId", cacheManager = "redisCacheManager")
    public void updateCartProduct(String userId, String productId, OrderDto.updateDto updateDto) {
        Optional<OrderProductCart> original_cart = getOrderProductCart(userId);

        if (original_cart.isPresent()) {
            OrderProduct product = orderProductRepository.findByUniqueNumber(productId)
                    .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404));

            product.updateProductInfo(updateDto);
        }
    }

    @Cacheable(value = "vLLC", key = "'cart:' + #userId", cacheManager = "redisCacheManager")
    public List<OrderDto.productInfoDto> getCartProducts(String userId) {
        return orderProductCartRepository.findAll(userId);
    }

    @DefaultRock(key = "#productId")
    public void deleteCartProduct(String userId, String productId) {
        Optional<OrderProductCart> original_cart = getOrderProductCart(userId);

        if (original_cart.isPresent()) {
            OrderProduct product = orderProductRepository.findByUniqueNumber(productId)
                    .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404));

            orderProductRepository.delete(product);
            return;
        }
        throw new CustomException(ErrorCode.ERROR_400);
    }

    private void addProductToCart(OrderDto.createDto productDto, OrderProductCart cart) {
        ProductEntity product = productRepository.findById(Long.valueOf(productDto.getProductId()))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404));

        OrderProduct orderProduct = OrderProduct.createOrders(productDto, product);
        orderProduct.setOrderProductCart(cart);
        cart.addProduct(orderProduct);

        cart.updateTotalProducts(cart.getOrderProducts().size());
        orderProductCartRepository.save(cart);
    }

    private Optional<OrderProductCart> getOrderProductCart(String userId) {
        return orderProductCartRepository.findByCreatedBy(userId);
    }
}
