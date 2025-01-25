package com.moblie.management.global.redis.repository;

import com.moblie.management.global.redis.domain.IdempotencyToken;
import org.springframework.data.repository.CrudRepository;

public interface IdempotencyRepository extends CrudRepository<IdempotencyToken, String> {
}
