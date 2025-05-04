package com.moblie.management.local.order.controller;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.jwt.dto.PrincipalDetails;
import com.moblie.management.global.utils.Response;
import com.moblie.management.local.member.service.MemberService;
import com.moblie.management.local.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final MemberService memberService;
    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<Response> newOrder(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam(name = "cart") String cartId) {

        isAccess(principalDetails.getEmail());
        orderService.createOrder(principalDetails.getId(), cartId);

        return ResponseEntity.ok(new Response("주문 완료"));
    }

    @PostMapping("/order/approve")
    public ResponseEntity<Response> approveOrder(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam(name = "order") String trackingId) {
        isAccess(principalDetails.getEmail());

        log.info("trackingId {}", trackingId);
        orderService.orderApproval(trackingId);

        return ResponseEntity.ok(new Response("승인 완료"));
    }

    private void isAccess(String email) {
        if (memberService.isAccess(email)) {
            throw new CustomException(ErrorCode.ERROR_409, "권한이 없는 사용자 입니다.");
        }
    }

}
