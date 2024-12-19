package com.moblie.management.redis.domain;

import com.moblie.management.product.dto.ProductDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter @Setter
@RedisHash(value = "product", timeToLive = 600)
public class ProductToken {
    @Id
    private String productName;
    private String factory;
    private String modelClassification;
    private String goldType;
    private String goldColor;
    private String modelWeight;
    private String modelNote;
}
