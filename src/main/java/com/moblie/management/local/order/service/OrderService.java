package com.moblie.management.local.order.service;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.order.dto.OrderDto;
import com.moblie.management.local.order.model.Order;
import com.moblie.management.local.order.model.OrderProduct;
import com.moblie.management.local.order.model.OrderProductCart;
import com.moblie.management.local.order.repository.order_product_cart.OrderProductCartRepository;
import com.moblie.management.local.order.repository.order_product.OrderProductRepository;
import com.moblie.management.local.order.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderProductCartRepository orderProductCartRepository;

    //장바구니값 주문
    @Transactional
    public void createOrder(String userId, String cartId) {

        OrderProductCart cart = orderProductCartRepository.findByCreatedByAndId(userId, Long.valueOf(cartId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404));

        Order order = Order.create(cart);

        List<OrderProduct> products = orderProductRepository.findByOrderProductCart(cart);
        for (OrderProduct product : products) {
            product.deletedCart();
            product.setOrder(order);
            order.addProduct(product);
        }
        orderRepository.save(order);
        orderProductCartRepository.delete(cart);

    }

    //주문 상품 승인 & 미승인 리스트 (승인/미승인 카테고리 검색)

    //주문 상품 승인
    @Transactional
    public void orderApproval(String trackingId) {
        Order order = orderRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404));
        order.updateStatus();
    }

    public PageCustom<OrderDto.productDto> getOrderList(String storeName, String startDate, String endDate, Pageable pageable) {
        return orderRepository.findByOrderProducts(storeName, startDate, endDate, pageable);
    }
}
