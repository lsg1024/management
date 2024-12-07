package com.moblie.management.redis.service;

import com.moblie.management.redis.domain.RefreshToken;
import com.moblie.management.redis.repository.RefreshRepository;
import lombok.RequiredArgsConstructor;
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

    public void createNewToken(String username, String token) {
        log.info("createNewToken");
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsername(username);
        refreshToken.setTokenValue(token);
        create(refreshToken);
    }

    public void updateNewToken(String username, String token) {
        log.info("updateNewToken");
        RefreshToken refreshToken = refreshRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("재로그인이 필요합니다"));

        refreshToken.setTokenValue(token);
        create(refreshToken);
    }

    public boolean existsToken(String username) {
        return exist(username);
    }

    public void deleteToken(String username) {
        delete(username);
    }
}
