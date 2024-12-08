package com.moblie.management.member.service;

import com.moblie.management.exception.CustomException;
import com.moblie.management.jwt.JwtUtil;
import com.moblie.management.member.domain.MemberEntity;
import com.moblie.management.member.dto.MemberDto;
import com.moblie.management.member.repository.MemberRepository;
import com.moblie.management.redis.domain.RefreshToken;
import com.moblie.management.redis.service.RedisRefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RedisRefreshTokenService redisRefreshTokenService;

    @BeforeEach
    @DisplayName("테스트 용 Member 생성")
    void init(){
        MemberDto.SignUp memberDto = new MemberDto.SignUp();
        memberDto.setEmail("test@gamil.com");
        memberDto.setNickname("memberTestUser");
        memberDto.setPassword("test_password");
        memberDto.setPassword_confirm("test_password");

        memberService.signUp(memberDto);
    }

    @AfterEach
    void clearAll() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUpMemberSuccess() {
        //given
        boolean existsByEmail = memberRepository.existsByEmail("test@gamil.com");

        //then
        assertTrue(existsByEmail);
    }

    @Test
    @DisplayName("회원가입 실패")
    void signUpMemberFail() {
        //given
        MemberDto.SignUp memberDto = new MemberDto.SignUp();
        memberDto.setEmail("test@gamil.com");
        memberDto.setNickname("memberTestUser");
        memberDto.setPassword("test_password");
        memberDto.setPassword_confirm("test_password");

        //when & then
        assertThrows(CustomException.class, () ->
                memberService.signUp(memberDto), "이미 가입된 이메일 정보 입니다.");
    }

    @Test
    @DisplayName("토큰 재발급")
    void reissueRefreshToken() {
        //given
        MemberEntity member = memberRepository.findByEmail("test@gamil.com");

        //when
        String refresh = jwtUtil.createJwt("refresh", member.getUserid().toString(), member.getEmail(), member.getRole().toString(), 1000L);
        redisRefreshTokenService.createNewToken("test@gamil.com", refresh);

        String[] tokens = memberService.reissueRefreshToken(refresh);
        assertSame(tokens.length, 2);

        log.info(tokens[0] + " " + tokens[1]);

        Optional<RefreshToken> refreshToken = redisRefreshTokenService.get("test@gamil.com");
        String email = refreshToken.get().getEmail();
        String tokenValue = refreshToken.get().getTokenValue();

        log.info("email: {} tokenValue: {}", email, tokenValue);

        redisRefreshTokenService.deleteToken("test@gamil.com");
    }

}