package com.moblie.management.member.service;

import com.moblie.management.exception.CustomException;
import com.moblie.management.exception.ErrorCode;
import com.moblie.management.jwt.JwtUtil;
import com.moblie.management.member.domain.MemberEntity;
import com.moblie.management.member.domain.Role;
import com.moblie.management.member.dto.MemberDto;
import com.moblie.management.member.repository.MemberRepository;
import com.moblie.management.redis.domain.CertificationNumberToken;
import com.moblie.management.redis.service.CertificationNumberService;
import com.moblie.management.redis.service.RedisRefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

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
    private final JavaMailSender javaMailSender;
    private final CertificationNumberService certificationNumberService;
    private final RedisRefreshTokenService redisRefreshTokenService;

    @Transactional
    public void signUp(MemberDto.SignUp signUp) {

        String email = signUp.getEmail().toLowerCase();

        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.ERROR_400, "이미 가입된 이메일 정보 입니다.");
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
    public String[] reissueRefreshToken(String refreshToken) {
        log.info("reissueRefreshToken");

        String refresh = verificationRefreshToken(refreshToken);

        String id = jwtUtil.getUserId(refresh);
        String email = jwtUtil.getEmail(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccessToken = jwtUtil.createJwt("access", id, email, role, ACCESS_TTL);
        String newRefreshToken = jwtUtil.createJwt("refresh", id, email, role, REFRESH_TTL);

        redisRefreshTokenService.updateNewToken(email, newRefreshToken);

        return new String[]{newAccessToken, newRefreshToken};

    }

    public void sendEmail(MemberDto.ChangePasswordDto newPasswordDto) {

        boolean existsByPassword = memberRepository.existsByPassword(encoder.encode(newPasswordDto.getPassword()));

        if (existsByPassword) {
            throw new CustomException(ErrorCode.ERROR_400, "동일한 비밀번호는 사용할 수 없습니다.");
        }

        checkConfirmPassword(newPasswordDto.getPassword(), newPasswordDto.getPassword_confirm());

        Random random = new Random();

        String certificationNumbers = randomNumbers(random);

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(newPasswordDto.getEmail());
        mailMessage.setSubject("비밀번호 변경을 위한 인증번호");
        mailMessage.setText(certificationNumbers);

        certificationNumberService.createToken(newPasswordDto.getEmail(), certificationNumbers, newPasswordDto.getPassword());
        javaMailSender.send(mailMessage);

    }

    public void checkCertificationNumbers(String email, String certificationNumber) {
        Optional<CertificationNumberToken> token = certificationNumberService.getToken(email);

        String randomValue = token
                .map(CertificationNumberToken::getRandomValue)
                .orElseThrow(() -> new IllegalArgumentException("유효기간이 지났습니다."));

        if (!randomValue.equals(certificationNumber)) {
            throw new CustomException(ErrorCode.ERROR_404, "번호가 일치하지 않습니다");
        }

    }

    private String verificationRefreshToken(String refresh) {
        log.info("verificationRefreshToken email 정보 {}", jwtUtil.getEmail(refresh));
        checkRefreshToken(jwtUtil.getEmail(refresh));

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.ERROR_400, "Refresh 토큰 만료");
        }

        String category = jwtUtil.getCategory(refresh);

        checkRefreshTokenType(category);

        return refresh;
    }

    private static String randomNumbers(Random random) {
        StringBuilder randomNumber = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            randomNumber.append(random.nextInt(10));
        }

        return randomNumber.toString();
    }

}
