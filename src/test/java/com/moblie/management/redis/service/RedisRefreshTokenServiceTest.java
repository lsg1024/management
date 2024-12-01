package com.moblie.management.redis.service;

import com.moblie.management.jwt.JwtUtil;
import com.moblie.management.jwt.dto.RefreshToken;
import com.moblie.management.jwt.repository.RefreshRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedisRefreshTokenServiceTest {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RedisRefreshTokenService redisRefreshTokenService;

    @Autowired
    RefreshRepository refreshRepository;

    @Test
    void createNewToken() {
        //given
        String username = "test5";
        String tokenValue = jwtUtil.createJwt("refresh", username, "test_name", "USER", 259200L);

        // when
        redisRefreshTokenService.createNewToken(username, tokenValue);

        // then
        Optional<RefreshToken> retrievedToken = refreshRepository.findByUsername(username);
        assertThat(retrievedToken).isNotNull();
        assertThat(retrievedToken.get().getUsername()).isEqualTo(username);
        assertThat(retrievedToken.get().getTokenValue()).isEqualTo(tokenValue);
    }

    @Test
    void removeUserTokens() {
        //given
        String username = "test5";

        //when
        redisRefreshTokenService.deleteToken(username);

        //then
        Optional<RefreshToken> refreshToken = refreshRepository.findByUsername(username);
        assertThat(refreshToken).isEmpty();
    }
}