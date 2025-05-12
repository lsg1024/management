package com.moblie.management.local.order.repository.order;

import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.order.dto.OrderDto;
import com.moblie.management.local.order.model.Order;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {

    PageCustom<Order> findTrackingId(String trackingId);
    PageCustom<OrderDto.productDto> findByOrderProducts(String storeName, String startDate, String endDate, Pageable pageable);

}
