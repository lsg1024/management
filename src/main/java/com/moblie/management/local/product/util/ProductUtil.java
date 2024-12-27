package com.moblie.management.local.product.util;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.local.product.domain.ProductImageEntity;
import com.moblie.management.local.product.dto.ProductDto;
import com.moblie.management.local.product.repository.ProductImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class ProductUtil {

    private static final String FILE_PATH = "/Users/imjaeseong/work_file/BackEnd/management";

    public static void formatProductExcelData(Sheet workSheet, ProductDto.productsInfo productsInfo, List<Integer> targetColum) {

        productsInfo.products = new ArrayList<>();

        for (int i = 1; i < workSheet.getPhysicalNumberOfRows(); i++) {
            Row row = workSheet.getRow(i);

            if (row == null) continue;

            List<String> columValues = targetColum.stream()
                    .map(index -> extractedExcelCellData(row, index))
                    .toList();

            ProductDto.createProduct product = new ProductDto.createProduct(
                    columValues.get(0), // modelNumber
                    columValues.get(1).toUpperCase(), // factory
                    columValues.get(2), // modelClassification
                    columValues.get(3), // goldType
                    columValues.get(4), // goldColor
                    columValues.get(5), // modelWeight
                    columValues.get(6)  // modelComment
            );

            productsInfo.products.add(product);

        }
    }

    private static String extractedExcelCellData(Row row, int target) {
        Cell cell = row.getCell(target);
        return cell != null ? cell.getStringCellValue() : null;
    }

    public static List<ProductImageEntity> formattingImage(String productName, List<MultipartFile> images, ProductImageRepository productImageRepository) throws IOException {

        List<ProductImageEntity> productImageEntities = new ArrayList<>();

        if (images.isEmpty()) {
            return productImageEntities;
        }

        String path = FILE_PATH + "/images/" + productName;
        File file = new File(path);

        log.info("파일 생성 경로 {}", path);

        if (!file.exists()) {
            if (file.mkdir()) {
                log.error("mkdir 이미지 폴더 생성");
            } else {
                throw new CustomException(ErrorCode.ERROR_404, "이미지 폴더 생성 실패");
            }
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String current_date = simpleDateFormat.format(new Date());

        for (MultipartFile image : images) {
            String contentType = image.getContentType();
            String originalFileExtension;

            if (ObjectUtils.isEmpty(contentType)) {
                continue;
            } else {
                if (contentType.contains("image/jpeg")) {
                    originalFileExtension = ".jpg";
                } else if (contentType.contains("image/png")) {
                    originalFileExtension = ".png";
                } else if (contentType.contains("image/gif")) {
                    originalFileExtension = ".gif";
                } else {
                    continue; // 지원하지 않는 파일 타입이면 스킵
                }
            }

            String newFileName = UUID.randomUUID() + "_" + current_date;

            boolean existsByImageOriginName = productImageRepository.existsByImageOriginName(image.getOriginalFilename());

            if (existsByImageOriginName) {
                continue;
            }

            ProductImageEntity productImageEntity;

            if (productImageEntities.isEmpty()) {
                productImageEntity = ProductImageEntity.builder()
                        .imageName(newFileName)
                        .imagePath(path + "/" + newFileName)
                        .firstImagePath(path + "/" + newFileName)
                        .imageOriginName(image.getOriginalFilename())
                        .build();
            } else {
                productImageEntity = ProductImageEntity.builder()
                        .imageName(newFileName)
                        .imagePath(path + "/" + newFileName)
                        .imageOriginName(image.getOriginalFilename())
                        .build();
            }

            productImageEntities.add(productImageEntity);
            file = new File(path + "/" + newFileName + originalFileExtension);
            image.transferTo(file);
        }


        return productImageEntities;

    }

}
