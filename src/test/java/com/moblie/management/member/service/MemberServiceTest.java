package com.moblie.management.member.service;

import com.moblie.management.exception.CustomException;
import com.moblie.management.jwt.JwtUtil;
import com.moblie.management.member.domain.MemberEntity;
import com.moblie.management.member.dto.MemberDto;
import com.moblie.management.member.repository.MemberRepository;
import com.moblie.management.redis.domain.CertificationNumberToken;
import com.moblie.management.redis.domain.RefreshToken;
import com.moblie.management.redis.service.CertificationNumberService;
import com.moblie.management.redis.service.RedisRefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class MemberServiceTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RedisRefreshTokenService redisRefreshTokenService;

    @Autowired
    private CertificationNumberService certificationNumberService;

    private final static String EMAIL = "test@gmail.com";

    @BeforeEach
    @DisplayName("테스트용 Member 생성")
    void init(){
        MemberDto.SignUp memberDto = new MemberDto.SignUp();
        memberDto.setEmail(EMAIL);
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
        boolean existsByEmail = memberRepository.existsByEmail(EMAIL);

        //then
        assertTrue(existsByEmail);
    }

    @Test
    @DisplayName("회원가입 실패")
    void signUpMemberFail() {
        //given
        MemberDto.SignUp memberDto = new MemberDto.SignUp();
        memberDto.setEmail(EMAIL);
        memberDto.setNickname("memberTestUser");
        memberDto.setPassword("test_password");
        memberDto.setPassword_confirm("test_password");

        //when & then
        assertThrows(CustomException.class, () ->
                memberService.signUp(memberDto), "이미 가입된 이메일 정보 입니다.");
    }

    @Test
    @DisplayName("토큰 재발급")
    void reissueRefreshTokenSuccess() {
        //given
        MemberEntity member = memberRepository.findByEmail(EMAIL);

        //when
        String refresh = jwtUtil.createJwt("refresh", member.getUserid().toString(), member.getEmail(), member.getRole().toString(), 1000L);
        redisRefreshTokenService.createNewToken(member.getEmail(), refresh);

        String[] tokens = memberService.reissueRefreshToken(refresh);

        //then
        Optional<RefreshToken> refreshToken = redisRefreshTokenService.get(member.getEmail());
        String email = refreshToken.get().getEmail();
        String tokenValue = refreshToken.get().getTokenValue();

        assertEquals(email, member.getEmail());
        assertEquals(tokens[1], tokenValue);

        redisRefreshTokenService.deleteToken(member.getEmail());
    }

    @Test
    @DisplayName("토큰 발급 실패")
    void reissueRefreshTokenFail() {
        //given
        MemberEntity member = memberRepository.findByEmail(EMAIL);

        //when
        String refresh = jwtUtil.createJwt("refresh", member.getUserid().toString(), member.getEmail(), member.getRole().toString(), -1L);
        redisRefreshTokenService.createNewToken(member.getEmail(), refresh);

        //then
        assertThrows(ExpiredJwtException.class, () -> memberService.reissueRefreshToken(refresh));

    }

    @Test
    @DisplayName("이메일 전송 성공")
    @Order(1)
    void sendEmailSuccess() {
        //given
        MemberDto.Certification newMemberDto = new MemberDto.Certification();
        newMemberDto.setEmail("zksqazwsx@gmail.com");

        //when
        memberService.sendEmail(newMemberDto);

        //then
        Optional<CertificationNumberToken> token = certificationNumberService.getToken(newMemberDto.getEmail());
        assertEquals(token.get().getEmail(), newMemberDto.getEmail());

    }

    @Test
    @DisplayName("인증번호 인증 성공")
    @Order(2)
    void certificationSuccess() {
        //given
        MemberDto.Certification newMemberDto = new MemberDto.Certification();
        newMemberDto.setEmail("zksqazwsx@gmail.com");

        Optional<CertificationNumberToken> token = certificationNumberService.getToken(newMemberDto.getEmail());

        //when & then
        assertDoesNotThrow(() -> memberService.certificationNumbers(token.get().getEmail(), token.get().getRandomValue()));

    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void updatePasswordSuccess() {
        // given
        MemberEntity member = memberRepository.findByEmail(EMAIL);
        String bfPassword = member.getPassword();
        UUID uuid = UUID.randomUUID();

        MemberDto.UpdatePassword passwordDto = new MemberDto.UpdatePassword();
        passwordDto.setPassword("new_password");
        passwordDto.setPassword_confirm("new_password");

        // when
        certificationNumberService.createToken(member.getEmail(), String.valueOf(uuid));
        memberService.updatePassword(member.getEmail(), String.valueOf(uuid), passwordDto);

        // then
        MemberEntity updatedMember = memberRepository.findByEmail(member.getEmail());
        assertNotEquals(bfPassword, updatedMember.getPassword());
    }

    @Test
    @DisplayName("비밀번호 변경 실패 (dto 불일치)")
    void updateInconsistencyPassword() {
        //given
        MemberEntity member = memberRepository.findByEmail(EMAIL);
        UUID uuid = UUID.randomUUID();

        MemberDto.UpdatePassword passwordDto = new MemberDto.UpdatePassword();
        passwordDto.setPassword("new_password");
        passwordDto.setPassword_confirm("not_new_password");

        //when
        certificationNumberService.createToken(member.getEmail(), String.valueOf(uuid));

        //then
        assertThrows(CustomException.class, () ->
                memberService.updatePassword(member.getEmail(), String.valueOf(uuid), passwordDto));
    }

    @Test
    @DisplayName("비밀번호 변경 실패 (token 만료)")
    void updateExistToken() {
        //given
        MemberEntity member = memberRepository.findByEmail(EMAIL);
        UUID uuid = UUID.randomUUID();
        MemberDto.UpdatePassword passwordDto = new MemberDto.UpdatePassword();
        passwordDto.setPassword("new_password");
        passwordDto.setPassword_confirm("new_password");

        //when
        certificationNumberService.createToken(member.getEmail(), String.valueOf(uuid));
        certificationNumberService.deleteToken(member.getEmail());

        //then
        assertThrows(CustomException.class, () ->
                memberService.updatePassword(member.getEmail(), String.valueOf(uuid), passwordDto));
    }



}