package com.moblie.management.local.order.controller;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.jwt.dto.PrincipalDetails;
import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.global.utils.Response;
import com.moblie.management.local.member.service.MemberService;
import com.moblie.management.local.order.dto.OrderDto;
import com.moblie.management.local.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    /**
     * @param trackingId
     * 관리자 권한만 접근 가능
     */
    @PostMapping("/order/approve")
    public ResponseEntity<Response> approveOrder(
            @RequestParam(name = "order") String trackingId) {

        orderService.orderApproval(trackingId);

        return ResponseEntity.ok(new Response("승인 완료"));
    }

    //주문 취소
    @DeleteMapping("/order/{tracking_id}")
    public ResponseEntity<Response> cancelOrder(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "tracking_id") String trackingId) {

        isAccess(principalDetails.getEmail());
        orderService.cancelOrder(trackingId);

        return ResponseEntity.ok(new Response("주문 취소 완료"));
    }

    //주문된 상품 목록
    @GetMapping("/order/{tracking_id}/products")
    public PageCustom<OrderDto.orderProducts> orderProducts(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "tracking_id") String trackingId,
            @PageableDefault(size = 20) Pageable pageable) {

        isAccess(principalDetails.getEmail());

        return orderService.getOrders(trackingId, pageable);
    }

    //주문 상품 상세 정보
    @GetMapping("/order/products/{order_product_tracking_number}")
    public ResponseEntity<OrderDto.orderProducts> orderProductInfo(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "order_product_tracking_number") String opt_number) {

        isAccess(principalDetails.getEmail());

        OrderDto.orderProducts productInfo = orderService.getOrderProductInfo(opt_number);

        return ResponseEntity.ok(productInfo);
    }

    //주문된 상품 수정
    @PatchMapping("/order/products/{tracking_id}")
    public ResponseEntity<Response> updateOrderProduct(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "tracking_id") String trackingId,
            @RequestBody @Valid OrderDto.updateDto dto) {

        isAccess(principalDetails.getEmail());
        orderService.orderInfoDetailUpdate(trackingId, dto);

        return ResponseEntity.ok(new Response("수정 완료"));
    }

    //주문된 상품 취소
    @DeleteMapping("/order/products/{tracking_id}")
    public ResponseEntity<Response> cancelOrderProduct(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "tracking_id") String trackingId) {

        isAccess(principalDetails.getEmail());
        orderService.orderInfoDetailCancel(trackingId);

        return ResponseEntity.ok(new Response("상품 삭제 완료"));
    }

    private void isAccess(String email) {
        if (memberService.isAccess(email)) {
            throw new CustomException(ErrorCode.ERROR_409, "권한이 없는 사용자 입니다.");
        }
    }

}
