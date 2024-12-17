package com.moblie.management.member.controller;

import com.moblie.management.member.dto.MemberDto;
import com.moblie.management.member.dto.Response;
import com.moblie.management.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.moblie.management.member.util.MemberUtil.createCookie;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final static Long REFRESH_TTL = 259200L;

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Response> singUp(@RequestBody @Valid  MemberDto.SignUp signUp) {

        memberService.signUp(signUp);

        return ResponseEntity.ok(new Response("회원가입 완료"));

    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")) {
                refresh = cookie.getValue();
                log.info("cookie refresh {}", refresh);
            }
        }
        String[] tokens = memberService.reissueRefreshToken(refresh);

        response.setHeader("Authorization", "Bearer " + tokens[0]);
        response.addCookie(createCookie("refreshToken", tokens[1], REFRESH_TTL.intValue()));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //인증번호 요청
    @PostMapping("/signin/find/password/email")
    public ResponseEntity<Response> certificationRequest(@RequestBody @Valid MemberDto.Certification certification) {

        memberService.sendEmail(certification);

        return ResponseEntity.ok(new Response(certification.getEmail()));
    }

    //인증번호 인증
    @PostMapping("/signin/find/password/certification")
    public ResponseEntity<Response> emailCertification(
            @RequestParam("email") String email,
            @RequestParam("certification") String certificationNumber) {

        String uuid = memberService.certificationNumbers(email, certificationNumber);

        return ResponseEntity.ok(new Response("인증 완료:" + uuid));
    }

    //비밀번호 변경
    @PostMapping("/signin/find/password")
    public ResponseEntity<Response> updatePassword(
            @RequestParam("email") String email,
            @RequestParam("token") String token,
            @RequestBody @Valid MemberDto.UpdatePassword passwordDto) {

        memberService.updatePassword(email, token, passwordDto);

        return ResponseEntity.ok(new Response("변경 완료"));
    }

    //회원탈퇴 (추가 비밀번호 인증 -> 세션 제거 -> redisToken 제거 -> soft delete)
    @DeleteMapping("/mypage")
    public ResponseEntity<Response> deleteMember(
            @RequestParam("userid") String userid,
            @RequestBody @Valid MemberDto.DeleteMember memberDto) {

        memberService.deleteMember(userid, memberDto);

        return ResponseEntity.ok(new Response("회원 탈퇴 완료"));
    }

    /*
       관리자 전용 명령어 ADMIN
       - 회원 권한 변경 -> WAIT -> USER
       -
     */

}
