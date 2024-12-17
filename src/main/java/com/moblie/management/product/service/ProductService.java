package com.moblie.management.product.service;

import com.moblie.management.exception.CustomException;
import com.moblie.management.exception.ErrorCode;
import com.moblie.management.factory.domain.FactoryEntity;
import com.moblie.management.factory.repository.FactoryRepository;
import com.moblie.management.product.domain.ProductEntity;
import com.moblie.management.product.dto.ProductDto;
import com.moblie.management.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static com.moblie.management.product.excel.ExcelProduct.formatProductExcelData;
import static com.moblie.management.product.validation.ProductValidation.*;
import static com.moblie.management.utils.ExcelSheetUtil.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FactoryRepository factoryRepository;

    //상품 엑셀 파일 자동호출
    @Transactional
    public List<String> createAutoProduct(MultipartFile file) throws IOException {
        Workbook workbook = getSheets(file);

        Sheet worksheet = workbook.getSheetAt(0);

        ProductDto.productsInfo productsInfo = new ProductDto.productsInfo();

        formatProductExcelData(worksheet, productsInfo, Arrays.asList(2, 3, 6, 7, 13, 8, 12));

        return createProductProcess(productsInfo);
    }
    
    //상품 수동 입력
    @Transactional
    public List<String> createManualProduct(ProductDto.productsInfo productsInfo) {

        return createProductProcess(productsInfo);
    }

    // 상품 정보 수정
    @Transactional
    public void updateProduct(String productId, ProductDto.productUpdate updateDto) {
        ProductEntity product = productRepository.findById(Long.parseLong(productId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404, "업데이트에 실패하였습니다."));

        validateProductName(productRepository, updateDto.getProductName());

        product.productUpdate(updateDto);
    }

    //상품 조회
//    @Transactional
//    public void searchProduct(String )

    //상품 삭제
    @Transactional
    public void deletedProduct(String productId) {
        ProductEntity product = productRepository.findById(Long.valueOf(productId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404, "유저 삭제를 실패하였습니다"));
        productRepository.delete(product);

    }

    private List<String> createProductProcess(ProductDto.productsInfo productsInfo) {
        List<ProductEntity> products = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        extractedProductInfo(productsInfo, products, errors);

        productRepository.saveAll(products);
        return errors;
    }

    //데이터 엔티티 리스트로 추출
    private void extractedProductInfo(ProductDto.productsInfo productsInfo, List<ProductEntity> products, List<String> errorProduct) {
        Set<String> errorSet = new HashSet<>();
        for (ProductDto.createProduct productDto : productsInfo.products) {
            if (validateUnionProductName(errorSet, productDto)) {
                validateFactoryAndSave(products, productDto);
            }
        }
        errorProduct.addAll(errorSet);
    }

    //예외를 발생시 해당 데이터들을 모아서 반환
    private boolean validateUnionProductName(Set<String> errors, ProductDto.createProduct productDto) {
        validateExistsProductNameOrProductBarcodeNumber(productRepository, productDto.getModelName(), productDto.getModelBarcode(), errors);
        validateExistsFactoryName(factoryRepository, productDto.getFactory(), errors);
        return !errors.isEmpty();
    }

    //공장 존재 확인 후 저장
    private void validateFactoryAndSave(List<ProductEntity> products, ProductDto.createProduct productDto) {
        FactoryEntity factory = factoryRepository.findByFactoryName(productDto.getFactory());
        if (factory != null) {
            products.add(productDto.toEntity(factory));
        }
    }
}
