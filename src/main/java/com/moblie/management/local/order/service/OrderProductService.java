package com.moblie.management.local.order.service;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.local.order.dto.OrderDto;
import com.moblie.management.local.order.model.OrderProduct;
import com.moblie.management.local.order.model.OrderProductCart;
import com.moblie.management.local.order.repository.OrderProductCartRepository;
import com.moblie.management.local.order.repository.OrderProductRepository;
import com.moblie.management.local.product.model.ProductEntity;
import com.moblie.management.local.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class OrderProductService {

    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderProductCartRepository orderProductCartRepository;

    public OrderProductCart createProductsOrder(OrderDto.ProductsDto productsDto) {

        OrderProductCart cart = OrderProductCart.builder()
                .orderProduct(new ArrayList<>())
                .build();

        orderProductCartRepository.save(cart);

        for (OrderDto.ProductDto productDto : productsDto.getProductsDto()) {
            ProductEntity product = productRepository.findById(Long.valueOf(productDto.getProductId()))
                    .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404));

            OrderProduct order = OrderProduct.createOrders(productDto, product);
            order.setOrderProductCart(cart);
            orderProductRepository.save(order);

            cart.addProduct(order);
        }

        return orderProductCartRepository.save(cart);
    }

}
