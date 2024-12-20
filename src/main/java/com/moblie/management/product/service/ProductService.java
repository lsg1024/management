package com.moblie.management.product.service;

import com.moblie.management.exception.CustomException;
import com.moblie.management.exception.ErrorCode;
import com.moblie.management.factory.domain.FactoryEntity;
import com.moblie.management.factory.repository.FactoryRepository;
import com.moblie.management.jwt.dto.PrincipalDetails;
import com.moblie.management.member.domain.MemberEntity;
import com.moblie.management.member.repository.MemberRepository;
import com.moblie.management.product.domain.ProductEntity;
import com.moblie.management.product.dto.ProductDto;
import com.moblie.management.product.repository.ProductRepository;
import com.moblie.management.product.validation.ProductValidation;
import com.moblie.management.redis.service.RedisProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final MemberRepository memberRepository;
    private final RedisProductService redisProductService;

    //상품 엑셀 파일 자동호출
    @Transactional
    public List<String> createAutoProduct(PrincipalDetails principalDetails, MultipartFile file) throws IOException {
        Workbook workbook = getSheets(file);

        Sheet worksheet = workbook.getSheetAt(0);

        ProductDto.productsInfo productsInfo = new ProductDto.productsInfo();

        formatProductExcelData(worksheet, productsInfo, Arrays.asList(2, 3, 6, 7, 13, 8, 12));

        return createProductProcess(productsInfo, principalDetails.getId());
    }
    
    //상품 수동 입력
    @Transactional
    public List<String> createManualProduct(PrincipalDetails principalDetails, ProductDto.productsInfo productsInfo) {

        return createProductProcess(productsInfo, principalDetails.getId());
    }

    // 상품 정보 수정
    @Transactional
    public void updateProduct(PrincipalDetails principalDetails, String productId, ProductDto.productUpdate updateDto) {
        validateExistsProductForMember(productRepository, principalDetails, productId);

        ProductEntity product = productRepository.findById(Long.parseLong(productId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404, "업데이트에 실패하였습니다."));

        validateProductName(productRepository, updateDto.getProductName());
        FactoryEntity factory = factoryRepository.findByFactoryName(updateDto.getFactory());

        product.productUpdate(updateDto, factory);
    }

    //상품 조회
    public Page<ProductDto.productSearchResult> searchProduct(ProductDto.productCondition condition, Pageable pageable) {
        return productRepository.searchProduct(condition, pageable);
    }

    //상품 삭제
    @Transactional
    public void deletedProduct(PrincipalDetails principalDetails, String productId) {
        validateExistsProductForMember(productRepository, principalDetails, productId);

        ProductEntity product = productRepository.findById(Long.valueOf(productId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404, "유저 삭제를 실패하였습니다"));
        productRepository.delete(product);
    }

    /**
     * 상품 생성 통합 메서드 createAutoProduct, createManualProduct
     * @param productsInfo products
     * @param memberId 사용자 id
     * @return 에러 데이터
     */
    private List<String> createProductProcess(ProductDto.productsInfo productsInfo, String memberId) {
        List<ProductEntity> products = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        extractedProductInfo(productsInfo, products, memberId, errors);

        productRepository.saveAll(products);
        return errors;
    }

    //데이터 엔티티 리스트로 추출
    private void extractedProductInfo(ProductDto.productsInfo productsInfo, List<ProductEntity> products, String memberId, List<String> errorProduct) {
        Set<String> errorSet = new HashSet<>();

        MemberEntity member = memberRepository.findById(Long.valueOf(memberId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_409, "유저 정보 오류"));

        for (ProductDto.createProduct productDto : productsInfo.products) {
            ProductValidation.validateUnionProductName(productRepository, factoryRepository, productDto, errorSet);

            if (errorSet.isEmpty()) {
                validateFactoryAndSave(factoryRepository, products, member, productDto);
            }
        }
        errorProduct.addAll(errorSet);
    }

}
