package com.moblie.management.jwt.handler;

import com.moblie.management.jwt.JwtUtil;
import com.moblie.management.redis.service.RedisRefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.moblie.management.member.util.MemberUtil.createCookie;


@Component
@RequiredArgsConstructor
@Slf4j
public class LoginUtil {

    private final JwtUtil jwtUtil;
    private final RedisRefreshTokenService redisRefreshTokenService;

    private final static Long ACCESS_TTL = 900L;
    private final static Long REFRESH_TTL = 259200L;

    public void handleSuccessfulAuthentication(String userId, String username, String role, HttpServletResponse response) throws IOException {
        // WAIT 권한의 사용자는 처리하지 않음
        if (role.equals("WAIT")) {
            response.sendRedirect("/login?error");
            return;
        }

        log.info("handleSuccessfulAuthentication");

        // 토큰 생성
        String accessToken = jwtUtil.createJwt("access", userId, username, role, ACCESS_TTL);
        String refreshToken = jwtUtil.createJwt("refresh", userId, username, role, REFRESH_TTL);

        // 리프레시 토큰 DB 저장
        redisRefreshTokenService.createNewToken(username, refreshToken);

        // 응답 헤더 및 쿠키 설정
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(createCookie("refreshToken", refreshToken, REFRESH_TTL.intValue()));
        response.setStatus(HttpStatus.OK.value());
    }

}
