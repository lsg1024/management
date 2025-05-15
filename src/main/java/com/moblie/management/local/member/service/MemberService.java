package com.moblie.management.local.member.service;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.jwt.JwtUtil;
import com.moblie.management.local.member.validation.MemberValidation;
import com.moblie.management.local.member.model.MemberEntity;
import com.moblie.management.local.member.model.Role;
import com.moblie.management.local.member.dto.MemberDto;
import com.moblie.management.local.member.repository.MemberRepository;
import com.moblie.management.global.redis.domain.CertificationNumberToken;
import com.moblie.management.global.redis.service.CertificationNumberService;
import com.moblie.management.global.redis.service.RedisRefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static com.moblie.management.global.redis.validation.TokenValidation.*;
import static com.moblie.management.local.member.util.MemberUtil.randomNumbers;

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

    public boolean isAccess(String email) {
        String sessionEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String sessionRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();

        if (sessionRole.equals("ROLE_ADMIN")) {
            return true;
        }

        return sessionEmail.equals(email);
    }

    @Transactional
    public void signUp(MemberDto.SignUp signUp) {

        String email = signUp.getEmail().toLowerCase();

        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.ERROR_400, "이미 가입된 이메일 정보 입니다.");
        }

        if (memberRepository.existsByNickname(signUp.getNickname())) {
            throw new CustomException(ErrorCode.ERROR_400, "동일한 이름이 존재합니다.");
        }

        MemberValidation.validateConfirmPassword(signUp.getPassword(), signUp.getPassword_confirm());

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

    public void sendEmail(MemberDto.MemberEmail memberEmail) {

        Random random = new Random();

        String certificationNumbers = randomNumbers(random);

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(memberEmail.getEmail());
        mailMessage.setSubject("비밀번호 변경을 위한 인증번호");
        mailMessage.setText("유효시간은 3분 입니다.\n" + certificationNumbers);

        certificationNumberService.createToken(memberEmail.getEmail(), certificationNumbers);
        javaMailSender.send(mailMessage);

    }

    public String certificationNumbers(String email, String certificationNumber) {
        Optional<CertificationNumberToken> token = certificationNumberService.getToken(email);

        String redisCertificationToken = validToken(token);

        validateCertificationNumber(redisCertificationToken, certificationNumber);

        UUID uuid = UUID.randomUUID();

        certificationNumberService.createToken(email, String.valueOf(uuid));

        return String.valueOf(uuid);
    }

    @Transactional
    public void updatePassword(String email, String token, MemberDto.UpdatePassword passwordDto) {
        Optional<CertificationNumberToken> certification_token = certificationNumberService.getToken(email);

        String redisCertificationToken = validToken(certification_token);

        validateCertificationNumber(redisCertificationToken, token);

        MemberEntity member = memberRepository.findByEmail(email);

        MemberValidation.validateConfirmPassword(passwordDto.getPassword(), passwordDto.getPassword_confirm());
        member.updatePassword(encoder.encode(passwordDto.getPassword()));

        certificationNumberService.deleteToken(email);
    }

    @Transactional
    public void deleteMember(String userid, MemberDto.DeleteMember memberDto) {
        Optional<MemberEntity> member = Optional.of(memberRepository.findById(Long.valueOf(userid))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404)));

        boolean passwordMatch = encoder.matches(memberDto.getPassword(), member.get().getPassword());

        if (!passwordMatch) {
            throw new CustomException(ErrorCode.ERROR_400, "잘못된 비밀번호 입니다.");
        }

        redisRefreshTokenService.deleteToken(member.get().getEmail());

        member.get().softDelete();
    }

    private String verificationRefreshToken(String refresh) {
        validateRefreshToken(jwtUtil.getEmail(refresh));

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.ERROR_407, "토큰 만료");
        }

        String category = jwtUtil.getCategory(refresh);

        validateRefreshTokenType(category);

        return refresh;
    }

    private String validToken(Optional<CertificationNumberToken> token) {
        return token
                .map(CertificationNumberToken::getRandomValue)
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_401, "유효기간이 지났습니다."));
    }

}
