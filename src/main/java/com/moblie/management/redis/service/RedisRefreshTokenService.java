package com.moblie.management.redis.service;

import com.moblie.management.exception.CustomException;
import com.moblie.management.exception.ErrorCode;
import com.moblie.management.jwt.dto.RefreshToken;
import com.moblie.management.jwt.repository.RefreshRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisRefreshTokenService {

    private final RefreshRepository refreshRepository;

    public void createNewToken(String username, String token) {
        log.info("createNewToken");
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsername(username);
        refreshToken.setTokenValue(token);
        refreshRepository.save(refreshToken);
    }

    public void updateNewToken(String username, String token) {
        log.info("updateNewToken");
        RefreshToken refreshToken = refreshRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("재로그인이 필요합니다"));

        refreshToken.setTokenValue(token);
        refreshRepository.save(refreshToken);
    }

    public boolean existsToken(String username) {
       return refreshRepository.existsByUsername(username);
    }

    public void deleteToken(String username) {
        refreshRepository.deleteById(username);
    }

}
