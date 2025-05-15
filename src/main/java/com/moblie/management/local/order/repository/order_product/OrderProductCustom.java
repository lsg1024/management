package com.moblie.management.local.order.repository.order_product;

import com.moblie.management.local.order.dto.OrderDto;

import java.util.Optional;

public interface OrderProductCustom {
    Optional<OrderDto.orderProducts> findByOptNumber(String opt_number);
}
