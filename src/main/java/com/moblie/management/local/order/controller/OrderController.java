package com.moblie.management.local.order.controller;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.jwt.dto.PrincipalDetails;
import com.moblie.management.local.member.service.MemberService;
import com.moblie.management.local.order.dto.OrderDto;
import com.moblie.management.local.order.model.OrderProductCart;
import com.moblie.management.local.order.service.OrderProductService;
import com.moblie.management.local.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final MemberService memberService;
    private final OrderProductService orderProductService;
    private final OrderService orderService;
    @PostMapping("/product/order")
    public ResponseEntity<?> newOrder(
            @RequestBody @Valid OrderDto.ProductsDto productsDto) {

        OrderProductCart products = orderProductService.createProductsOrder(productsDto);
        orderService.createOrderRequest(products);

        return ResponseEntity.ok("성공");
    }


}
