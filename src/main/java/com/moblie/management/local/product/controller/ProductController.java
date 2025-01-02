package com.moblie.management.local.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.jwt.dto.PrincipalDetails;
import com.moblie.management.local.member.service.MemberService;
import com.moblie.management.local.product.dto.ProductDto;
import com.moblie.management.local.product.dto.ProductResponse;
import com.moblie.management.local.product.service.ProductImageService;
import com.moblie.management.local.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final MemberService memberService;
    private final ProductService productService;
    private final ProductImageService productImageService;

    @PostMapping("/products/new")
    public ResponseEntity<ProductResponse> uploadExcelFile(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam MultipartFile file) throws IOException {

        List<String> errors = productService.createAutoProduct(principalDetails, file);

        return errors.isEmpty() ? ResponseEntity.ok(new ProductResponse("저장 완료")) : ResponseEntity.ok(new ProductResponse("중복 데이터가 존재합니다", errors));
    }

    /**
     * @param images // 이미지 파일
     * @param products // 단일 생성할지 복수 생성할지 고민 중...
     * @return 저장되지 않은 값들 상품 이름 반환
     */
    @PostMapping(value = "/product/new")
    public ResponseEntity<ProductResponse> createProduct(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam("products") String products) throws IOException {

        ProductDto.productsInfo productsDto = new ObjectMapper().readValue(products, ProductDto.productsInfo.class);
        List<String> errors = productService.createManualProduct(principalDetails, productsDto);

        productImageService.createImages(productsDto, images);

        return errors.isEmpty() ? ResponseEntity.ok(new ProductResponse("저장 완료")) : ResponseEntity.ok(new ProductResponse("중복 데이터가 존재합니다", errors));
    }

    @PatchMapping("/product/{productId}/edit")
    public ResponseEntity<ProductResponse> updateProduct(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("productId") String productId,
            @RequestBody @Valid ProductDto.productUpdate updateDto) {

        isAccess(principalDetails.getEmail());

        productService.updateProduct(productId, updateDto);

        return ResponseEntity.ok(new ProductResponse("수정 완료"));
    }

    @PatchMapping("/product/{productId}/image")
    public ResponseEntity<ProductResponse> updateProductImage(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("productId") String productId,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) throws IOException {

        isAccess(principalDetails.getEmail());
        productImageService.updateImages(productId, images);

        return ResponseEntity.ok(new ProductResponse("수정 완료"));
    }

    @GetMapping("/product/search")
    public Page<ProductDto.productSearchResult> searchProduct(
            @RequestParam("product") String productName,
            @RequestParam("factory") String factoryName,
            @RequestParam("classification") String classification,
            @PageableDefault(size = 16)Pageable pageable) {

        ProductDto.productCondition condition = new ProductDto.productCondition(
                productName,
                factoryName,
                classification
        );

        return productService.searchProducts(condition, pageable);
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<ProductResponse> deleteProduct(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable String productId) {
        isAccess(principalDetails.getEmail());
        productService.deletedProduct(productId);
        return ResponseEntity.ok(new ProductResponse("삭제 완료"));
    }

    @DeleteMapping("/product/{productId}/{image}")
    public ResponseEntity<ProductResponse> deleteProductImage(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(value = "productId") String productId,
            @PathVariable(value = "image") String imageId) {

        isAccess(principalDetails.getEmail());
        productImageService.deleteImage(productId, imageId);

        return ResponseEntity.ok(new ProductResponse("삭제 완료"));
    }

    private void isAccess(String email) {
        if (!memberService.isAccess(email)) {
            throw new CustomException(ErrorCode.ERROR_409, "권한이 없는 사용자 입니다.");
        }
    }

}
