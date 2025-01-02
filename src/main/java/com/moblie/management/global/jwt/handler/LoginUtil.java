package com.moblie.management.global.jwt.handler;

import com.moblie.management.global.jwt.JwtUtil;
import com.moblie.management.global.redis.service.RedisRefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.moblie.management.local.member.util.MemberUtil.createCookie;


@Component
@RequiredArgsConstructor
@Slf4j
public class LoginUtil {

    private final JwtUtil jwtUtil;
    private final RedisRefreshTokenService redisRefreshTokenService;

    private final static Long ACCESS_TTL = 3 * 24 * 60 * 60L;
    private final static Long REFRESH_TTL = 14 * 24 * 60 * 60L;

    public void handleSuccessfulAuthentication(String userId, String email, String role, HttpServletResponse response) throws IOException {
        // WAIT 권한의 사용자는 처리하지 않음
        if (role.equals("WAIT")) {
            response.sendRedirect("/login?error");
            return;
        }

        log.info("handleSuccessfulAuthentication");

        // 토큰 생성
        String accessToken = jwtUtil.createJwt("access", userId, email, role, ACCESS_TTL);
        String refreshToken = jwtUtil.createJwt("refresh", userId, email, role, REFRESH_TTL);

        // 리프레시 토큰 DB 저장
        redisRefreshTokenService.createNewToken(email, refreshToken);

        // 응답 헤더 및 쿠키 설정
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(createCookie("refreshToken", refreshToken, REFRESH_TTL.intValue()));
        response.setStatus(HttpStatus.OK.value());
    }

}
