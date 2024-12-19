package com.moblie.management.redis.service;

import com.moblie.management.product.dto.ProductDto;
import com.moblie.management.redis.domain.ProductToken;
import com.moblie.management.redis.repository.RedisProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;

@Slf4j
@Service
public class RedisProductService extends RedisCrudService<ProductToken, String> {


    private final RedisProductRepository redisProductRepository;

    protected RedisProductService(RedisProductRepository redisProductRepository) {
        super(redisProductRepository);
        this.redisProductRepository = redisProductRepository;
    }

    public void createNewProduct(String productId, ProductDto.createProduct product) {
        log.info("createNewProduct Token");
        ProductToken productToken = new ProductToken();
        productToken.setProductName(product.getProductName());
        productToken.setFactory(product.getFactory());
        productToken.setModelClassification(product.getModelClassification());
        productToken.setGoldType(product.getGoldType());
        productToken.setGoldColor(product.getGoldColor());
        productToken.setModelNote(product.getModelNote());

        create(productToken);
    }

//    public void updateNewProduct(String productId, ProductDto.createProduct product) {
//
//    }

    //페이징 처리 레디스
//    public Option<Page>

}
