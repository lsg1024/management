package com.moblie.management.jwt.repository;

import com.moblie.management.jwt.dto.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByUsername(String username);
}