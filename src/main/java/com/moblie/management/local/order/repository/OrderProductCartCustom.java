package com.moblie.management.local.order.repository;

import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.order.dto.OrderDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderProductCartCustom {
    List<OrderDto.productInfoDto> findAll(String userId);
}
