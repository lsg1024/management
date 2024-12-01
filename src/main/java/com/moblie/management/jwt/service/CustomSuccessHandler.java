package com.moblie.management.jwt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moblie.management.jwt.JwtUtil;
import com.moblie.management.jwt.dto.PrincipalDetails;
import com.moblie.management.redis.service.RedisRefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final static Long ACCESS_TTL = 900L;
    private final static Long REFRESH_TTL = 259200L;
    private final JwtUtil jwtUtil;
    private final RedisRefreshTokenService redisRefreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        PrincipalDetails customUserDetails = (PrincipalDetails) authentication.getPrincipal();

        String userId = customUserDetails.getId();
        String username = customUserDetails.getName();
        String role = customUserDetails.getRole();

        // 토큰 생성
        String accessToken = jwtUtil.createJwt("access", userId, username, role, ACCESS_TTL);
        String refreshToken = jwtUtil.createJwt("refresh", userId, username, role, REFRESH_TTL);

        // 리프레시 토큰 DB 저장
        redisRefreshTokenService.createNewToken(username, refreshToken);

        responseJsonToken(response, accessToken, refreshToken);
    }

    private void responseJsonToken(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("accessToken", accessToken);
        response.addCookie(createCookie("refreshToken", refreshToken));
        response.sendRedirect("/loginSuccess");
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(1000 * 60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

}

