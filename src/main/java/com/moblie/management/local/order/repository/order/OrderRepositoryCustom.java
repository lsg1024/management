package com.moblie.management.local.order.repository.order;

import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.order.model.Order;

import java.util.List;

public interface OrderRepositoryCustom {

    PageCustom<Order> findByTrackingId(String trackingId);

}
