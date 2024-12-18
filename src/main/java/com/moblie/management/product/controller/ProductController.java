package com.moblie.management.product.controller;

import com.moblie.management.member.dto.Response;
import com.moblie.management.product.domain.ProductEntity;
import com.moblie.management.product.dto.ProductDto;
import com.moblie.management.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products/new")
    public ResponseEntity<Response> uploadExcelFile(@RequestParam MultipartFile file) throws IOException {

        List<String> errors = productService.createAutoProduct(file);

        if (!errors.isEmpty()) {
            return ResponseEntity.ok(new Response("중복 데이터가 존재합니다", errors));
        }

        return ResponseEntity.ok(new Response("저장완료"));
    }

    @PostMapping("/product/new")
    public ResponseEntity<Response> createProduct(@RequestBody @Valid ProductDto.productsInfo productsInfo) {

        List<String> errors = productService.createManualProduct(productsInfo);

        if (!errors.isEmpty()) {
            return ResponseEntity.ok(new Response("중복 데이터가 존재합니다", errors));
        }

        return ResponseEntity.ok(new Response("저장 완료"));
    }

    @PostMapping("/product/{productId}/edit")
    public ResponseEntity<Response> updateProduct(
            @PathVariable("productId") String productId,
            @RequestBody @Valid ProductDto.productUpdate updateDto) {

        productService.updateProduct(productId, updateDto);

        return ResponseEntity.ok(new Response("수정 완료"));
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

        return productService.searchProduct(condition, pageable);
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Response> deleteProduct(@PathVariable String productId) {
        productService.deletedProduct(productId);
        return ResponseEntity.ok(new Response("삭제 완료"));
    }

}
