package com.moblie.management.local.product.validation;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.local.factory.domain.FactoryEntity;
import com.moblie.management.local.factory.repository.FactoryRepository;
import com.moblie.management.global.jwt.dto.PrincipalDetails;
import com.moblie.management.local.member.domain.MemberEntity;
import com.moblie.management.local.product.domain.ProductEntity;
import com.moblie.management.local.product.dto.ProductDto;
import com.moblie.management.local.product.repository.ProductRepository;

import java.util.List;
import java.util.Set;

public class ProductValidation {

    public static void validateProductName(ProductRepository productRepository, String productName) {
        if (productRepository.existsByProductName(productName)) {
            throw new CustomException(ErrorCode.ERROR_404, "이미 존재하는 이름입니다.");
        }
    }
    public static void validateUnionProductName(ProductRepository productRepository, FactoryRepository factoryRepository, ProductDto.createProduct productDto, Set<String> errors) {
        validateExistsProductNameOrProductBarcodeNumber(productRepository, productDto.getProductName(), productDto.getModelBarcode(), errors);
        validateExistsFactoryName(factoryRepository, productDto.getFactory(), errors);
    }
    public static void validateExistsProductNameOrProductBarcodeNumber(ProductRepository productRepository, String modelName, String barcode, Set<String> errors) {
        if (productRepository.existsByProductNameOrProductBarcodeNumber(modelName, barcode)) {
            errors.add("중복된 모델 이름과 바코드: " + modelName);
        }
    }

    public static void validateExistsFactoryName(FactoryRepository factoryRepository, String factoryName, Set<String> errors) {
        if (!factoryRepository.existsByFactoryName(factoryName)) {
            errors.add("존재하지 않는 제조사: " + factoryName);
        }
    }
    public static void validateFactoryAndSave(FactoryRepository factoryRepository, List<ProductEntity> products, MemberEntity member, ProductDto.createProduct productDto) {
        FactoryEntity factory = factoryRepository.findByFactoryName(productDto.getFactory());
        if (factory != null) {
            products.add(productDto.toEntity(factory, member));
        }
    }
    public static void validateProductAccess(ProductRepository productRepository, PrincipalDetails principalDetails, String productId) {
        if (principalDetails.getRole().equals("ADMIN")) {
            return;
        }

        boolean productExists = productRepository.existsByMemberIdAndProductId(principalDetails.getId(), productId);
        if (!productExists) {
            throw new CustomException(ErrorCode.ERROR_403, "잘못된 데이터 정보가 들어왔습니다.");
        }
    }


}
