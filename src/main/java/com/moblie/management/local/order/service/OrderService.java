package com.moblie.management.local.order.service;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.order.dto.OrderDto;
import com.moblie.management.local.order.model.Order;
import com.moblie.management.local.order.model.OrderProduct;
import com.moblie.management.local.order.model.OrderProductCart;
import com.moblie.management.local.order.repository.order_product.OrderProductCustom;
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
    private final OrderProductCustom orderProductCustom;

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

    //주문 상품 승인
    @Transactional
    public void orderApproval(String trackingId) {
        Order order = orderRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404));
        order.updateStatusApproval();
    }

    public PageCustom<OrderDto.productDto> getOrderList(String storeName, String startDate, String endDate, Pageable pageable) {
        return orderRepository.findByOrderProducts(storeName, startDate, endDate, pageable);
    }

    public PageCustom<OrderDto.orderProducts> getOrderInfo(String trackingId, Pageable pageable) {
        return orderRepository.findByOrderReadyProducts(trackingId, pageable);
    }

    //주문 상품 정보 변경
    @Transactional
    public void orderInfoDetailUpdate(String trackingId, OrderDto.updateDto dto) {
        OrderProduct orderProduct = orderProductRepository.findByOrderProductTrackingNumber(trackingId).orElseThrow(() ->
                new CustomException(ErrorCode.ERROR_404, "존재하지 않은 주문 정보 입니다."));

        orderProduct.updateProductInfo(dto);
    }

    @Transactional
    public void cancelOrder(String trackingId) {
        orderRepository.findByTrackingId(trackingId).orElseThrow(() ->
                new CustomException(ErrorCode.ERROR_404, "존재하지 않은 주문 정보 입니다."));

        orderRepository.cancelOrderByTrackingId(trackingId);
        orderProductCustom.softDeletedByOrderTrackingId(trackingId);
    }

    // 지금 trackingId 값은 주문장이므로 개별 Id 값으로 변경해야 됨
    @Transactional
    public void orderInfoDetailCancel(String trackingId) {
        orderProductCustom.softDeletedByOrderTrackingId(trackingId);
    }

    //주문 제품 상세 조회
    public OrderDto.orderProducts getOrderProductInfo(String optNumber) {
        return orderProductCustom.findByOptNumber(optNumber).orElseThrow(() ->
                new CustomException(ErrorCode.ERROR_404, "존재하지 않은 상품 정보 입니다."));
    }
}
