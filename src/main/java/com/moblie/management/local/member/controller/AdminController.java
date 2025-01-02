package com.moblie.management.local.member.controller;

import com.moblie.management.local.member.dto.MemberDto;
import com.moblie.management.local.member.dto.Response;
import com.moblie.management.local.member.service.AdminService;
import com.moblie.management.local.product.dto.ProductDto;
import com.moblie.management.local.product.dto.ProductResponse;
import com.moblie.management.local.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final ProductService productService;

    //일반 유저 권한 수정 (WAIT -> USER)
    @PatchMapping("/member/role")
    public ResponseEntity<Response> memberRoleUpdate(
            @RequestBody @Valid MemberDto.MemberEmail updateMemberInfo) {

        adminService.updateMemberRole(updateMemberInfo);

        return ResponseEntity.ok(new Response("수정 완료"));
    }

    //일반 유저 삭제
    @DeleteMapping("/member")
    public ResponseEntity<Response> memberDelete(
            @RequestBody @Valid MemberDto.MemberEmail updateMemberInfo) {

        adminService.deleteMember(updateMemberInfo);

        return ResponseEntity.ok(new Response("삭제 완료"));
    }

    //상품 데이터 수정
    @PatchMapping("/product/{productId}/edit")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable("productId") String productId,
            @RequestBody @Valid ProductDto.productUpdate updateDto) {

        productService.updateProduct(productId, updateDto);

        return ResponseEntity.ok(new ProductResponse("수정 완료"));
    }

    //상품 데이터 삭제
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<ProductResponse> deleteProduct(
            @PathVariable String productId) {
        productService.deletedProduct(productId);

        return ResponseEntity.ok(new ProductResponse("삭제 완료"));
    }

}
