package com.moblie.management.local.order.controller;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.jwt.dto.PrincipalDetails;
import com.moblie.management.local.member.service.MemberService;
import com.moblie.management.local.order.dto.OrderDto;
import com.moblie.management.local.order.service.OrderProductCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderProductCartController {

    private final MemberService memberService;
    private final OrderProductCartService orderProductCartService;

    @PostMapping("/cart")
    public ResponseEntity<?> newCart(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid OrderDto.createDto productDto) {

        log.info("newCart Controller");
        orderProductCartService.createProductCart(principalDetails.getId(), productDto);

        return ResponseEntity.ok("성공");
    }

    //장바구니 상품 상세 내역을 수정 (ex 수량? 색상)
    @PatchMapping("/cart/{product_id}")
    public ResponseEntity<?> updateCartProduct(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "product_id") String uniqueNumber,
            @RequestBody @Valid OrderDto.updateDto productsDto) {

        isAccess(principalDetails.getEmail());
        log.info("updateCart Controller");
        orderProductCartService.updateCartProduct(principalDetails.getId(), uniqueNumber, productsDto);

        return ResponseEntity.ok("성공");
    }
    
    //장바구니 리스트
    @GetMapping("/cart")
    public List<OrderDto.productInfoDto> cartProducts(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("cartProducts");
        return orderProductCartService.getCartProducts(principalDetails.getId());
    }

    //장바구나 상품 삭제
    @DeleteMapping("/cart/{product_id}")
    public ResponseEntity<?> deleteCartProduct(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "product_id") String uniqueNumber) {
        isAccess(principalDetails.getEmail());
        orderProductCartService.deleteCartProduct(principalDetails.getId(), uniqueNumber);

        return ResponseEntity.ok("성공");
    }

    private void isAccess(String email) {
        if (memberService.isAccess(email)) {
            throw new CustomException(ErrorCode.ERROR_409, "권한이 없는 사용자 입니다.");
        }
    }

}
