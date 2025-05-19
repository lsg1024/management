package com.moblie.management.redis.service;

import com.moblie.management.global.redis.service.RedisRefreshTokenService;
import com.moblie.management.global.jwt.JwtUtil;
import com.moblie.management.global.redis.domain.RefreshToken;
import com.moblie.management.global.redis.repository.RefreshRepository;
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
        String tokenValue = jwtUtil.createJwt("refresh", userid, "test@gamil.com", "nickname", "USER", 259200L);

        // when
        redisRefreshTokenService.createNewToken(userid, tokenValue);

        // then
        Optional<RefreshToken> retrievedToken = refreshRepository.findByEmail(userid);
        assertThat(retrievedToken).isNotNull();
        assertThat(retrievedToken.get().getEmail()).isEqualTo("test@gamil.com");
        assertThat(retrievedToken.get().getTokenValue()).isEqualTo(tokenValue);
    }

    @Test
    void removeUserTokens() {
        //when
        redisRefreshTokenService.deleteToken("zks1415@naver.com");

        //then
        Optional<RefreshToken> refreshToken = refreshRepository.findByEmail(userid);
        assertThat(refreshToken).isEmpty();
    }
}