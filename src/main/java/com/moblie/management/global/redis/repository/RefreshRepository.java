package com.moblie.management.global.redis.repository;

import com.moblie.management.global.redis.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByEmail(String email);
    boolean existsByEmail(String username);
}