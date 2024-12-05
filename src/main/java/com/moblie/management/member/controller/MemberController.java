package com.moblie.management.member.controller;

import com.moblie.management.jwt.JwtUtil;
import com.moblie.management.member.dto.MemberDto;
import com.moblie.management.member.dto.Response;
import com.moblie.management.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

import static com.moblie.management.member.util.MemberUtil.createCookie;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final static Long REFRESH_TTL = 259200L;

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Response> singUp(@Validated @RequestBody MemberDto.SignUp signUp, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }

            return ResponseEntity.badRequest().body(new Response("회원가입 실패", errors));
        }

        memberService.signUpMember(signUp);

        return ResponseEntity.ok(new Response("회원가입 완료"));

    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String[] tokens = memberService.reissueRefreshToken(request);

        response.setHeader("Authorization", "Bearer " + tokens[0]);
        response.addCookie(createCookie("refreshToken", tokens[1], REFRESH_TTL.intValue()));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //비밀번호 변경
//    @PostMapping("")

    //회원탈퇴

    /*
       관리자 전용 명령어 ADMIN
       - 회원 권한 변경 -> WAIT -> USER
       -
     */

}
