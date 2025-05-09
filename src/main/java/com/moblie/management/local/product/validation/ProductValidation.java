package com.moblie.management.local.product.validation;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.local.factory.repository.FactoryRepository;
import com.moblie.management.global.jwt.dto.PrincipalDetails;
import com.moblie.management.local.product.dto.ProductDto;
import com.moblie.management.local.product.model.ClassificationEntity;
import com.moblie.management.local.product.repository.ClassificationRepository;
import com.moblie.management.local.product.repository.ProductRepository;

import java.util.Set;

public class ProductValidation {

    public static void validateProductName(ProductRepository productRepository, String productName) {
        if (productRepository.existsByProductName(productName)) {
            throw new CustomException(ErrorCode.ERROR_404, "이미 존재하는 이름입니다.");
        }
    }
//    public static void validateProductDto(ProductRepository productRepository, FactoryRepository factoryRepository, ClassificationRepository classificationRepository, ProductDto.createProduct productDto, Set<String> errors) {
//        validateExistProductName(productRepository, productDto.getProductName(), errors);
////        validateExistBarcode(productRepository, productDto.getProductName(), productDto.getModelBarcode(), errors);
//        validateExistFactory(factoryRepository, productDto.getFactory(), errors);
//        validateExistClassification(classificationRepository, productDto.getModelClassification(), errors);
//    }
//    public static boolean validateExistProductName(ProductRepository productRepository, String modelName) {
//        if (productRepository.existsByProductName(modelName)) {
//            return false;
//        }
//        return true;
//    }

    public static void validateExistBarcode(ProductRepository productRepository, String modelName, String barcode, Set<String> errors) {
        if (productRepository.existsByProductBarcodeNumber(barcode)) {
            errors.add("중복된 바코드 명: " + modelName);
        }
    }

    public static void validateExistFactory(FactoryRepository factoryRepository, String factoryName, Set<String> errors) {
        if (!factoryRepository.existsByFactoryName(factoryName)) {
            errors.add("존재하지 않는 제조사: " + factoryName);
        }
    }

    public static void validateExistClassification(ClassificationRepository classificationRepository, String classificationName, Set<String> errors) {
        if (!classificationRepository.existsByClassificationName(classificationName)) {
            errors.add("존재하지 않는 카테고리: " + classificationName);
        }
    }

}
