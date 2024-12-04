package com.moblie.management.redis.service;

import com.moblie.management.jwt.JwtUtil;
import com.moblie.management.jwt.dto.RefreshToken;
import com.moblie.management.jwt.repository.RefreshRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedisRefreshTokenServiceTest {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RedisRefreshTokenService redisRefreshTokenService;

    @Autowired
    RefreshRepository refreshRepository;

    private String userid;

    @BeforeEach
    void createTestUser() {
        userid = UUID.randomUUID().toString();
    }
    

    @Test
    void createNewToken() {
        //given
        String tokenValue = jwtUtil.createJwt("refresh", userid, "test_name", "USER", 259200L);

        // when
        redisRefreshTokenService.createNewToken(userid, tokenValue);

        // then
        Optional<RefreshToken> retrievedToken = refreshRepository.findByUsername(userid);
        assertThat(retrievedToken).isNotNull();
        assertThat(retrievedToken.get().getUsername()).isEqualTo(userid);
        assertThat(retrievedToken.get().getTokenValue()).isEqualTo(tokenValue);
    }

    @Test
    void removeUserTokens() {
        //when
        redisRefreshTokenService.deleteToken(userid);

        //then
        Optional<RefreshToken> refreshToken = refreshRepository.findByUsername(userid);
        assertThat(refreshToken).isEmpty();
    }
}