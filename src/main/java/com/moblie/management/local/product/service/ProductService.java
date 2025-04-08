package com.moblie.management.local.product.service;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.local.factory.model.FactoryEntity;
import com.moblie.management.local.factory.repository.FactoryRepository;
import com.moblie.management.global.jwt.dto.PrincipalDetails;
import com.moblie.management.local.product.model.ClassificationEntity;
import com.moblie.management.local.product.model.ProductEntity;
import com.moblie.management.local.product.repository.ClassificationRepository;
import com.moblie.management.local.product.repository.ProductRepository;
import com.moblie.management.local.member.model.MemberEntity;
import com.moblie.management.local.member.repository.MemberRepository;
import com.moblie.management.local.product.dto.ProductDto;
import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.product.validation.ProductValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static com.moblie.management.global.utils.ExcelSheetUtil.*;
import static com.moblie.management.local.product.util.ProductUtil.formatProductExcelData;
import static com.moblie.management.local.product.validation.ProductValidation.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FactoryRepository factoryRepository;
    private final MemberRepository memberRepository;
    private final ClassificationRepository classificationRepository;

    //상품 엑셀 파일 자동호출
    @Transactional
    public List<String> createAutoProduct(PrincipalDetails principalDetails, MultipartFile file) throws IOException {
        Workbook workbook = getSheets(file);

        Sheet worksheet = workbook.getSheetAt(0);

        ProductDto.productsInfo productsInfo = new ProductDto.productsInfo();

        formatProductExcelData(worksheet, productsInfo, Arrays.asList(2, 3, 6, 7, 13, 8, 12));

        return createAutoProductProcess(productsInfo, principalDetails.getId());
    }
    
    //상품 수동 입력
    @Transactional
    public List<String> createManualProduct(PrincipalDetails principalDetails, ProductDto.productsInfo productsInfo) throws IOException {
        return createManualProductProcess(productsInfo, principalDetails.getId());
    }

    // 상품 정보 수정
    @Transactional
    public void updateProduct(String productId, ProductDto.productUpdate updateDto) {
        ProductEntity product = productRepository.findById(Long.parseLong(productId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404, "업데이트에 실패하였습니다."));
        product.validateProductAccess(productId);

        ProductValidation.validateProductName(productRepository, updateDto.getProductName());
        FactoryEntity factory = factoryRepository.findByFactoryName(updateDto.getFactory());
        ClassificationEntity classification = classificationRepository.findByClassificationName(updateDto.getModelClassification());

        product.productUpdate(updateDto, classification, factory);
    }

    //상품 조회
    @Cacheable(value = "sLC", key = "'productSearch' + #condition.productName + ':' + #condition.factory + ':' + #condition.modelClassification + ':' + #pageable.pageNumber", cacheManager = "redisCacheManager")
    public PageCustom<ProductDto.productSearchResult> searchProducts(ProductDto.productCondition condition, Pageable pageable) {
        return productRepository.searchProduct(condition, pageable);
    }

    //상품 삭제
    @Transactional
    public void deletedProduct(String productId) {
        ProductEntity product = productRepository.findById(Long.valueOf(productId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404, "상품 삭제를 실패하였습니다"));
        product.validateProductAccess(productId);
        product.delete();
    }

    private List<String> createAutoProductProcess(ProductDto.productsInfo productsInfo, String memberId) throws IOException {
        List<String> errors = new ArrayList<>();

        extractedProductInfo(productsInfo, memberId, errors);

        return errors;
    }

    private List<String> createManualProductProcess(ProductDto.productsInfo productsInfo, String memberId) throws IOException {
        List<String> errors = new ArrayList<>();

        extractedProductInfo(productsInfo, memberId, errors);

        return errors;
    }

    private void extractedProductInfo(ProductDto.productsInfo productsInfo, String memberId, List<String> errorProduct) throws IOException {
        Set<String> errorSet = new HashSet<>();

        MemberEntity member = memberRepository.findById(Long.valueOf(memberId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_409, "유저 정보 오류"));

        List<ProductEntity> productEntities = new ArrayList<>();
        for (ProductDto.createProduct productDto : productsInfo.products) {

            Set<String> error = new HashSet<>();
            validateProductDto(productRepository, factoryRepository, productDto, error);

            FactoryEntity factory = factoryRepository.findByFactoryName(productDto.getFactory());
            ClassificationEntity classification = classificationRepository.findByClassificationName(productDto.getModelClassification());

            if (classification == null) {
                classification = ClassificationEntity.builder()
                        .classificationName(productDto.getModelClassification())
                        .build();

                classificationRepository.save(classification);
            }

            if (error.isEmpty()) {
                ProductEntity productEntity = ProductEntity.create(productDto, member, classification, factory);
                productEntities.add(productEntity);
            } else {
                errorSet.addAll(error);
            }
        }
        if (!productEntities.isEmpty()) {
            productRepository.saveAll(productEntities);
        }

        errorProduct.addAll(errorSet);
    }

}
