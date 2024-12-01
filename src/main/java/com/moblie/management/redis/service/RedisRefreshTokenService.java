package com.moblie.management.redis.service;

import com.moblie.management.jwt.dto.RefreshToken;
import com.moblie.management.jwt.repository.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RedisRefreshTokenService {

    private final RefreshRepository refreshRepository;

    public void createNewToken(String username, String token) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsername(username);
        refreshToken.setTokenValue(token);
        refreshRepository.save(refreshToken);
    }

    public void updateNewToken(String username, String accessToken, String refreshToken) {

    }

    @Transactional
    public void deleteToken(String username) {
        refreshRepository.deleteById(username);
    }

}
