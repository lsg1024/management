package com.moblie.management.member.service;

import com.moblie.management.exception.CustomException;
import com.moblie.management.exception.ErrorCode;
import com.moblie.management.jwt.JwtUtil;
import com.moblie.management.member.domain.MemberEntity;
import com.moblie.management.member.domain.Role;
import com.moblie.management.member.dto.MemberDto;
import com.moblie.management.member.repository.MemberRepository;
import com.moblie.management.redis.service.RedisRefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.moblie.management.member.validation.MemberValidation.*;
import static com.moblie.management.redis.validation.TokenValidation.checkRefreshToken;
import static com.moblie.management.redis.validation.TokenValidation.checkRefreshTokenType;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final static Long ACCESS_TTL = 900L;
    private final static Long REFRESH_TTL = 259200L;

    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder;
    private final MemberRepository memberRepository;
    private final RedisRefreshTokenService redisRefreshTokenService;

    @Transactional
    public void signUpMember(MemberDto.SignUp signUp) {

        String email = signUp.getEmail().toLowerCase();

        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATE_FAILED);
        }

        checkConfirmPassword(signUp.getPassword(), signUp.getPassword_confirm());

        MemberEntity member = MemberEntity.builder()
                .email(signUp.getEmail())
                .nickname(signUp.getNickname())
                .password(encoder.encode(signUp.getPassword()))
                .role(Role.WAIT)
                .build();

        memberRepository.save(member);
    }

    @Transactional
    public String[] reissueRefreshToken(HttpServletRequest request) {

        log.info("reissueRefreshToken");
        String refresh = null;

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")) {
                refresh = cookie.getValue();
            }
        }

        checkRefreshToken(jwtUtil.getNickname(refresh));

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.REQUEST_FAILED, "Refresh 토큰 만료");
        }

        String category = jwtUtil.getCategory(refresh);

        checkRefreshTokenType(category);

        String id = jwtUtil.getUserId(refresh);
        String nickname = jwtUtil.getNickname(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccessToken = jwtUtil.createJwt("access", id, nickname, role, ACCESS_TTL);
        String newRefreshToken = jwtUtil.createJwt("refresh", id, nickname, role, REFRESH_TTL);

        redisRefreshTokenService.updateNewToken(nickname, newRefreshToken);

        return new String[]{newAccessToken, newRefreshToken};

    }

}
