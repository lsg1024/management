package com.moblie.management.global.redis.service;

import com.moblie.management.global.redis.domain.RefreshToken;
import com.moblie.management.global.redis.repository.RefreshRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisRefreshTokenService extends RedisCrudService<RefreshToken, String> {

    private final RefreshRepository refreshRepository;

    public RedisRefreshTokenService(RefreshRepository refreshRepository) {
        super(refreshRepository);
        this.refreshRepository = refreshRepository;
    }

    public void createNewToken(String email, String token) {
        log.info("createNewToken");
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setEmail(email);
        refreshToken.setTokenValue(token);
        create(refreshToken);
    }

    public void updateNewToken(String email, String token) {
        log.info("updateNewToken");
        RefreshToken refreshToken = refreshRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("재로그인이 필요합니다"));

        refreshToken.setTokenValue(token);
        create(refreshToken);
    }

    public boolean existsToken(String email) {
        return exist(email);
    }

    public void deleteToken(String email) {
        delete(email);
    }
}
