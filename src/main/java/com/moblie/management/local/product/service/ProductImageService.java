package com.moblie.management.local.product.service;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.jwt.dto.PrincipalDetails;
import com.moblie.management.local.product.domain.ProductEntity;
import com.moblie.management.local.product.domain.ProductImageEntity;
import com.moblie.management.local.product.dto.ProductDto;
import com.moblie.management.local.product.repository.ProductImageRepository;
import com.moblie.management.local.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductImageService {

    private static final String FILE_PATH = "/Users/imjaeseong/work_file/BackEnd/management";

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    @Transactional
    public void createImages(ProductDto.productsInfo productsInfo, List<MultipartFile> images) throws IOException {

        String productName = productsInfo.products.get(0).getProductName();
        ProductEntity product = productRepository.findByProductName(productName);

        if (images != null) {
            List<ProductImageEntity> productImageEntities = formattingImage(productsInfo.products.get(0).getProductName(), images, product);
            for (ProductImageEntity productImageEntity : productImageEntities) {
                product.addImage(productImageEntity);
                productImageRepository.save(productImageEntity);
            }
        }
    }

    @Transactional
    public void updateImages(String productId, List<MultipartFile> images) throws IOException {
        ProductEntity product = productRepository.findById(Long.valueOf(productId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_400, "찾을 수 없습니다."));

        product.validateProductAccess(productId);

        if (images != null) {
            List<ProductImageEntity> productImageEntities = formattingImage(product.generateImageFolderName(), images, product);
            for (ProductImageEntity productImageEntity : productImageEntities) {
                product.addImage(productImageEntity);
                productImageRepository.save(productImageEntity);
            }
        }
    }

    @Transactional
    public void deleteImage(String productId, String imageId) {
        ProductEntity product = productRepository.findById(Long.parseLong(productId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_400, "찾을 수 없습니다."));
        product.validateProductAccess(productId);
        productImageRepository.deleteByImageName(imageId);
    }

    private List<ProductImageEntity> formattingImage(String productName, List<MultipartFile> images, ProductEntity product) throws IOException {

        List<ProductImageEntity> productImageEntities = new ArrayList<>();

        if (images.isEmpty()) {
            return productImageEntities;
        }

        String path = FILE_PATH + "/images/" + productName;
        File file = new File(path);

        log.info("파일 생성 경로 {}", path);

        createImageFolder(file);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String current_date = simpleDateFormat.format(new Date());

        boolean existsByFirstImage = productImageRepository.existByFirstPathImage(product.getProductId());

        for (MultipartFile image : images) {
            String contentType = image.getContentType();
            String originalFileExtension;

            originalFileExtension = getImageContentType(contentType);
            if (originalFileExtension == null) continue; // 지원하지 않는 파일 타입이면 스킵

            String newFileName = UUID.randomUUID() + "_" + current_date;

            //기존 사진 이름과 동일한지 확인
            boolean existsByImageOriginName = productImageRepository.existsByImageOriginName(image.getOriginalFilename());

            createProductImageEntities(productImageEntities, path, image, originalFileExtension, newFileName, existsByImageOriginName, existsByFirstImage);
        }

        return productImageEntities;

    }

    private void createImageFolder(File file) {
        if (!file.exists()) {
            if (file.mkdir()) {
                log.error("mkdir 이미지 폴더 생성");
            } else {
                throw new CustomException(ErrorCode.ERROR_404, "이미지 폴더 생성 실패");
            }
        }
    }

    private String getImageContentType(String contentType) {
        String originalFileExtension;
        if (ObjectUtils.isEmpty(contentType)) {
            return null;
        } else {
            if (contentType.contains("image/jpeg")) {
                originalFileExtension = ".jpg";
            } else if (contentType.contains("image/png")) {
                originalFileExtension = ".png";
            } else if (contentType.contains("image/gif")) {
                originalFileExtension = ".gif";
            } else {
                return null;
            }
        }
        return originalFileExtension;
    }

    private void createProductImageEntities(List<ProductImageEntity> productImageEntities, String path, MultipartFile image, String originalFileExtension, String newFileName, boolean existsByImageOriginName, boolean existsByFirstImage) throws IOException {
        File file;
        if (!existsByImageOriginName) {

            ProductImageEntity productImageEntity;

            if (productImageEntities.isEmpty() && !existsByFirstImage) {
                String firstImagePath = path + "/" + newFileName;
                productImageEntity = ProductImageEntity.create(newFileName, image.getOriginalFilename(), path, firstImagePath);
            } else {
                productImageEntity = ProductImageEntity.create(newFileName, image.getOriginalFilename(), path, null);
            }

            productImageEntities.add(productImageEntity);
            file = new File(path + "/" + newFileName + originalFileExtension);
            image.transferTo(file);

        }
    }

}
