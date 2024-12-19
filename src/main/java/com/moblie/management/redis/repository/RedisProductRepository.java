package com.moblie.management.redis.repository;

import com.moblie.management.redis.domain.ProductToken;
import org.springframework.data.repository.CrudRepository;

public interface RedisProductRepository extends CrudRepository<ProductToken, String> {
}
