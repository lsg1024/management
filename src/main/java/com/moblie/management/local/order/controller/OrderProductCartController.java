package com.moblie.management.local.order.controller;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.jwt.dto.PrincipalDetails;
import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.member.service.MemberService;
import com.moblie.management.local.order.dto.CartDto;
import com.moblie.management.local.order.dto.OrderDto;
import com.moblie.management.local.order.dto.OrderResponse;
import com.moblie.management.local.order.service.OrderProductCartService;
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
public class OrderProductCartController {

    private final MemberService memberService;
    private final OrderProductCartService orderProductCartService;

    //새로운 장바구니 생성 후 장바구니 목록으로 리다이렉트
    @PostMapping("/cart/new")
    public ResponseEntity<?> newCart(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid CartDto.create newCart) {

        log.info("newCart Controller");
        orderProductCartService.createNewCart(principalDetails.getId(), newCart.getStoreId());

        return ResponseEntity.ok(new OrderResponse("성공"));
    }

    //장바구니에 상품 추가
    @PostMapping("/cart")
    public ResponseEntity<?> addProductToCart(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam(name = "id") String cartId,
            @RequestParam(name = "productId") String productId,
            @Valid @RequestBody CartDto.addProduct product) {

        isAccess(principalDetails.getEmail());
        orderProductCartService.addProductToCart(cartId, productId, product);

        return ResponseEntity.ok(new OrderResponse("성공"));
    }

    //장바구니 상품 상세 내역을 수정 (ex 수량? 색상)
    @PatchMapping("/cart")
    public ResponseEntity<?> updateCartProduct(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam(name = "trackingId") String trackingId,
            @RequestBody @Valid OrderDto.updateDto productsDto) {

        isAccess(principalDetails.getEmail());
        orderProductCartService.updateProductToCart(trackingId, productsDto);

        return ResponseEntity.ok(new OrderResponse("성공"));
    }
    
    //장바구니 리스트
    @GetMapping("/cart")
    public PageCustom<CartDto.carts> cartPage(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault() Pageable pageable) {

        return orderProductCartService.getCarts(principalDetails.getId(), pageable);
    }

    //장바구니 내 상품 리스트
    @GetMapping("/cart/info")
    public PageCustom<CartDto.productDetail> cartProductDetailPage(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam(name = "cart") String cartTrackingId,
            @PageableDefault(size = 20) Pageable pageable) {

        return orderProductCartService.getCartProductDetail(principalDetails.getId(), cartTrackingId, pageable);
    }

    //장바구나 상품 삭제
    @DeleteMapping("/cart/{cartId}")
    public ResponseEntity<?> deleteCartProduct(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "cartId") String cartId,
            @RequestParam(name = "product") String trackingId) {
        log.info("cartId: {}, product: {}", cartId, trackingId);
        isAccess(principalDetails.getEmail());
        orderProductCartService.deleteProductToCart(principalDetails.getId(), cartId, trackingId);

        return ResponseEntity.ok(new OrderResponse("성공"));
    }

    private void isAccess(String email) {
        if (memberService.isAccess(email)) {
            throw new CustomException(ErrorCode.ERROR_409, "권한이 없는 사용자 입니다.");
        }
    }

}
