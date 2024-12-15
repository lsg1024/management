package com.moblie.management.product.service;

import com.moblie.management.exception.CustomException;
import com.moblie.management.exception.ErrorCode;
import com.moblie.management.product.domain.ProductEntity;
import com.moblie.management.product.dto.ProductDto;
import com.moblie.management.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.moblie.management.product.excel.ExcelSheetUtils.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    //상품 엑셀 파일 자동호출
    @Transactional
    public List<String> createAutoProduct(MultipartFile file) throws IOException {
        Workbook workbook = getSheets(file);

        Sheet worksheet = workbook.getSheetAt(0);

        ProductDto.productsInfo productsInfo = new ProductDto.productsInfo();

        formatProductExcelData(worksheet, productsInfo, Arrays.asList(2, 3, 6, 7, 13, 8, 12));

        List<ProductEntity> products = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        extractedProductInfo(productsInfo, products, errors);

        productRepository.saveAll(products);

        return errors;
    }

    //상품 수동 입력
    @Transactional
    public List<String> createManualProduct(ProductDto.productsInfo productsInfo) {

        List<ProductEntity> products = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        extractedProductInfo(productsInfo, products, errors);

        productRepository.saveAll(products);

        return errors;
    }

    // 상품 정보 수정
    @Transactional
    public void updateProduct(String productId, ProductDto.productUpdate updateDto) {

        ProductEntity product = productRepository.findById(Long.parseLong(productId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404, "업데이트에 실패하였습니다."));
        product.productUpdate(updateDto);

    }

    //상품 조회
//    public

    //상품 삭제
    @Transactional
    public void deletedProduct(String productId) {
        Optional<ProductEntity> product = productRepository.findById(Long.valueOf(productId));
        if (product.isPresent()) {
            productRepository.delete(product.get());
        } else {
            throw new CustomException(ErrorCode.ERROR_404, "유저 삭제를 실패하였습니다");
        }
    }

    //데이터 엔티티 리스트로 추출
    private void extractedProductInfo(ProductDto.productsInfo productsInfo, List<ProductEntity> products, List<String> ErrorProduct) {
        for (ProductDto.createProduct productDto : productsInfo.products) {
            if (validateUnionProductName(ErrorProduct, productDto)) {
                products.add(productDto.toEntity());
            }
        }
    }

    //예외를 발생시 해당 데이터들을 모아서 반환
    private boolean validateUnionProductName(List<String> errors, ProductDto.createProduct productDto) {
        if (productRepository.existsByProductNameAndProductBarcodeNumber(productDto.getModelName(), productDto.getModelBarcode())) {
            errors.add(productDto.getModelName());
            return false;
        }
        return true;
    }


}
