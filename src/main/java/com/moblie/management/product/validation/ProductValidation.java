package com.moblie.management.product.validation;

import com.moblie.management.exception.CustomException;
import com.moblie.management.exception.ErrorCode;
import com.moblie.management.factory.domain.FactoryEntity;
import com.moblie.management.factory.repository.FactoryRepository;
import com.moblie.management.jwt.dto.PrincipalDetails;
import com.moblie.management.member.domain.MemberEntity;
import com.moblie.management.product.domain.ProductEntity;
import com.moblie.management.product.dto.ProductDto;
import com.moblie.management.product.repository.ProductRepository;

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
    public static void validateExistsProductForMember(ProductRepository productRepository, PrincipalDetails principalDetails, String productId) {
        if (!productRepository.existsByMemberIdAndProductId(principalDetails.getId(), productId)) {
            throw new CustomException(ErrorCode.ERROR_404, "잘못된 데이터 정보가 들어왔습니다.");
        }
    }

}
