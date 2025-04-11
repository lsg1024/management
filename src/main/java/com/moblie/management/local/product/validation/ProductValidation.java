package com.moblie.management.local.product.validation;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.local.factory.repository.FactoryRepository;
import com.moblie.management.local.product.dto.ProductDto;
import com.moblie.management.local.product.repository.ClassificationRepository;
import com.moblie.management.local.product.repository.ProductRepository;

import java.util.Set;

public class ProductValidation {

    public static void validateProductName(ProductRepository productRepository, String productName) {
        if (productRepository.existsByProductName(productName)) {
            throw new CustomException(ErrorCode.ERROR_404, "이미 존재하는 이름입니다.");
        }
    }

    public static void validateExistBarcode(ProductRepository productRepository, String modelName, String barcode, Set<String> errors) {
        if (productRepository.existsByProductBarcodeNumber(barcode)) {
            errors.add("중복된 바코드 명: " + modelName);
        }
    }


}
