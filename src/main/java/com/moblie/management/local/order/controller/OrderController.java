package com.moblie.management.local.order.controller;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.jwt.dto.PrincipalDetails;
import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.global.utils.Response;
import com.moblie.management.local.member.service.MemberService;
import com.moblie.management.local.order.dto.OrderDto;
import com.moblie.management.local.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final MemberService memberService;
    private final OrderService orderService;

    @GetMapping("/order")
    public PageCustom<OrderDto.productDto> orderList(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam(name = "store_name") String store_name,
            @RequestParam(name = "start_date") String startDate,
            @RequestParam(name = "end_date") String endDate,
            @PageableDefault Pageable pageable) {

        isAccess(principalDetails.getEmail());

        return orderService.getOrderList(store_name, startDate, endDate, pageable);
    }

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
            @RequestParam(name = "order") String trackingId) {

        isAdmin();

        log.info("trackingId {}", trackingId);
        orderService.orderApproval(trackingId);

        return ResponseEntity.ok(new Response("승인 완료"));
    }

    private void isAccess(String email) {
        if (memberService.isAccess(email)) {
            throw new CustomException(ErrorCode.ERROR_409, "권한이 없는 사용자 입니다.");
        }
    }

    private void isAdmin() {
        log.info("어드민 권한 검사 {}", memberService.isAdmin());
        if (!memberService.isAdmin()) {
            throw new CustomException(ErrorCode.ERROR_409, "권한이 없는 사용자 입니다.");
        }
    }

}
