package com.moblie.management.local.order.repository.order;

import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.order.dto.OrderDto;
import com.moblie.management.local.order.model.Order;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRepositoryCustom {

    PageCustom<OrderDto.orderInfoDto> findOrderInfo(OrderDto.orderCondition condition, Pageable pageable);

}
