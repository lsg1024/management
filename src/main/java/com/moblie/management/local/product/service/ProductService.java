package com.moblie.management.local.product.service;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.local.factory.model.FactoryEntity;
import com.moblie.management.local.factory.repository.FactoryRepository;
import com.moblie.management.global.jwt.dto.PrincipalDetails;
import com.moblie.management.local.product.model.ClassificationEntity;
import com.moblie.management.local.product.model.ColorEntity;
import com.moblie.management.local.product.model.MaterialEntity;
import com.moblie.management.local.product.model.ProductEntity;
import com.moblie.management.local.product.repository.ClassificationRepository;
import com.moblie.management.local.product.repository.ColorRepository;
import com.moblie.management.local.product.repository.MaterialRepository;
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

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FactoryRepository factoryRepository;
    private final MemberRepository memberRepository;
    private final ClassificationRepository classificationRepository;
    private final MaterialRepository materialRepository;
    private final ColorRepository colorRepository;

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
    public List<String> createManualProduct(PrincipalDetails principalDetails, ProductDto.productsInfo productsInfo) {
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
        MaterialEntity material = materialRepository.findByMaterialName(updateDto.getGoldType());
        ColorEntity color = colorRepository.findByColorName(updateDto.getGoldColor());

        product.productUpdate(updateDto, classification, material, color, factory);
    }

    //상품 조회
    @Cacheable(value = "sLC", key = "'productSearch' + #condition.productName + ':' + #condition.factory + ':' + #condition.modelClassification + ':' + #pageable.pageNumber", cacheManager = "redisCacheManager")
    public PageCustom<ProductDto.productDetailInfo> searchProducts(ProductDto.productCondition condition, Pageable pageable) {
        return productRepository.searchProduct(condition, pageable);
    }

    public ProductDto.productDetailInfo findProductManualDetail(String productId) {
        ProductEntity product = productRepository.findById(Long.valueOf(productId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404, "상품을 찾을 수 없습니다."));

        return product.getProductDetailInfo();
    }

    public ProductDto.productDetailInfo findProductDetail(String barcodeNumber) {
        ProductEntity product = productRepository.findByProductBarcodeNumber(barcodeNumber);

        return product.getProductDetailInfo();
    }

    //상품 삭제
    @Transactional
    public void deletedProduct(String productId) {
        ProductEntity product = productRepository.findById(Long.valueOf(productId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404, "상품 삭제를 실패하였습니다"));
        product.validateProductAccess(productId);
        productRepository.delete(product);
    }

    private List<String> createAutoProductProcess(ProductDto.productsInfo productsInfo, String memberId)  {
        List<String> errors = new ArrayList<>();

        extractedProductInfo(productsInfo, memberId, errors);

        return errors;
    }

    private List<String> createManualProductProcess(ProductDto.productsInfo productsInfo, String memberId){
        List<String> errors = new ArrayList<>();

        extractedProductInfo(productsInfo, memberId, errors);

        return errors;
    }

    private void extractedProductInfo(ProductDto.productsInfo productsInfo, String memberId, List<String> errorProduct)  {
        Set<String> errorSet = new HashSet<>();

        MemberEntity member = memberRepository.findById(Long.valueOf(memberId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_409, "유저 정보 오류"));

        List<ProductEntity> productEntities = new ArrayList<>();
        for (ProductDto.productInfo productDto : productsInfo.products) {

            Set<String> error = new HashSet<>();
            validation(productDto, error);

            FactoryEntity factory = factoryRepository.findByFactoryName(productDto.getFactory());
            ClassificationEntity classification = classificationRepository.findByClassificationName(productDto.getModelClassification());
            MaterialEntity material = materialRepository.findByMaterialName(productDto.getGoldType());
            ColorEntity color = colorRepository.findByColorName(productDto.getGoldColor());

            if (error.isEmpty()) {
                ProductEntity productEntity = ProductEntity.create(productDto, member, classification, material, color, factory);
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

    private void validation(ProductDto.productInfo productDto, Set<String> error) {
        if (productRepository.existsByProductName(productDto.getProductName())) {
            error.add("중복된 상품 이름: " + productDto.getProductName());
            return;
        }

        if (!factoryRepository.existsByFactoryName(productDto.getFactory())) {
            error.add("존재하지 않은 공장: " + productDto.getFactory());
            return;
        }

        if (!classificationRepository.existsByClassificationName(productDto.getModelClassification())) {
            error.add("존재하지 않는 카테고리: " + productDto.getModelClassification());
        }

    }

}
