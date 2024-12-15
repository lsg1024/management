package com.moblie.management.product.controller;

import com.moblie.management.member.dto.Response;
import com.moblie.management.product.dto.ProductDto;
import com.moblie.management.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/new/products")
    public ResponseEntity<Response> uploadExcelFile(@RequestParam MultipartFile file) throws IOException {

        List<String> errors = productService.createAutoProduct(file);

        if (!errors.isEmpty()) {
            return ResponseEntity.ok(new Response("중복 데이터가 존재합니다", errors));
        }

        return ResponseEntity.ok(new Response("저장완료"));
    }

    @PostMapping("new/product")
    public ResponseEntity<Response> createProduct(@RequestBody @Valid ProductDto.createProduct products) {


        return ResponseEntity.ok(new Response("저장 완료"));
    }

}
