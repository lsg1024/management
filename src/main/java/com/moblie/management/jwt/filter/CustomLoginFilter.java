package com.moblie.management.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moblie.management.exception.CustomException;
import com.moblie.management.exception.ErrorCode;
import com.moblie.management.jwt.JwtUtil;
import com.moblie.management.jwt.dto.PrincipalDetails;
import com.moblie.management.member.dto.MemberDto;
import com.moblie.management.member.dto.Response;
import com.moblie.management.redis.service.RedisRefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import static com.moblie.management.member.util.MemberUtil.createCookie;
import static com.moblie.management.member.validation.MemberValidation.*;

@Slf4j
@RequiredArgsConstructor
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final static Long ACCESS_TTL = 900L;
    private final static Long REFRESH_TTL = 259200L;

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final RedisRefreshTokenService redisRefreshTokenService;

    public CustomLoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, RedisRefreshTokenService redisRefreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.redisRefreshTokenService = redisRefreshTokenService;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MemberDto.SignIn signInRequest = objectMapper.readValue(request.getInputStream(), MemberDto.SignIn.class);

            checkEmail(signInRequest.getEmail());
            checkPassword(signInRequest.getPassword());

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword(), null);

            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.ERROR_400, "Json 형식이 잘못되었습니다.");
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        PrincipalDetails customUserDetails = (PrincipalDetails) authentication.getPrincipal();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 토큰 생성
        String accessToken = jwtUtil.createJwt("access", customUserDetails.getId(), customUserDetails.getEmail(), role, ACCESS_TTL);
        String refreshToken = jwtUtil.createJwt("refresh", customUserDetails.getId(), customUserDetails.getEmail(), role, REFRESH_TTL);

        // 리프레시 토큰 DB 저장
        redisRefreshTokenService.createNewToken(customUserDetails.getEmail(), refreshToken);

        // 응답 헤더 및 쿠키 설정
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(new Response("로그인 성공"));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(createCookie("refreshToken", refreshToken, REFRESH_TTL.intValue()));
        response.getWriter().write(jsonResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    }

}
