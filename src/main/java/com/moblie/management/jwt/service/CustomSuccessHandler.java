package com.moblie.management.jwt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moblie.management.jwt.JwtUtil;
import com.moblie.management.jwt.dto.PrincipalDetails;
//import com.moblie.management.redis.dto.RefreshToken;
//import com.moblie.management.redis.repository.RefreshRepository;
//import com.moblie.management.redis.service.RedisTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

//@Component
//@RequiredArgsConstructor
//public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    private final static Long ACCESS_TTL = 900L;
//    private final static Long REFRESH_TTL = 259200L;
//    private final JwtUtil jwtUtil;
//    private final RefreshRepository refreshRepository;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
//
//        PrincipalDetails customUserDetails = (PrincipalDetails) authentication.getPrincipal();
//
//        String userId = customUserDetails.getId();
//        String username = customUserDetails.getName();
//        String role = customUserDetails.getRole();
//
//        // 토큰 생성
//        String accessToken = jwtUtil.createJwt("access", userId, username, role, ACCESS_TTL);
//        String refreshToken = jwtUtil.createJwt("refresh", userId, username, role, REFRESH_TTL);
//
//        // 리프레시 토큰 DB 저장
//        createNewToken(username, refreshToken);
//
//        responseJsonToken(response, accessToken, refreshToken);
//    }
//
//    private void responseJsonToken(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, String> tokens = new HashMap<>();
//        tokens.put("accessToken", accessToken);
//        tokens.put("refreshToken", refreshToken);
//
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(objectMapper.writeValueAsString(tokens));
//    }
//
//    public void createNewToken(String username, String tokenValue) {
//        RefreshToken refreshToken = new RefreshToken();
//        refreshToken.setSeries(java.util.UUID.randomUUID().toString());
//        refreshToken.setUsername(username);
//        refreshToken.setTokenValue(tokenValue);
//        refreshToken.setLastUsed(System.currentTimeMillis());
//        refreshRepository.save(refreshToken);
//    }
//
//}

