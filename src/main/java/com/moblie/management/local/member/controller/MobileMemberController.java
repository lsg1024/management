package com.moblie.management.local.member.controller;

import com.moblie.management.global.jwt.JwtUtil;
import com.moblie.management.global.jwt.dto.KakaoMemberInfoResponse;
import com.moblie.management.global.jwt.service.CustomOAuth2UserService;
import com.moblie.management.global.redis.service.RedisRefreshTokenService;
import com.moblie.management.local.member.dto.LoginTokenDto;
import com.moblie.management.local.member.dto.MemberDto;
import com.moblie.management.local.member.model.MemberEntity;
import com.moblie.management.local.member.service.MobileMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MobileMemberController {

    private final static Long ACCESS_TTL = 3 * 24 * 60 * 60L;
    private final static Long REFRESH_TTL = 14 * 24 * 60 * 60L;

    private final JwtUtil jwtUtil;
    private final MobileMemberService mobileMemberService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final RedisRefreshTokenService redisRefreshTokenService;

    @PostMapping("/api/login")
    public ResponseEntity<LoginTokenDto.Response> mobileLogin(
            @RequestBody @Valid LoginTokenDto.Request request) {

        KakaoMemberInfoResponse kakaoInfo = mobileMemberService.kakaoApi(request.getAccessToken());
        MemberEntity member = customOAuth2UserService.processOAuth2User(kakaoInfo);

        MemberDto.MemberInfo memberInfo = member.getMemberInfo();
        String accessToken = jwtUtil.createJwt("access", memberInfo.getUserId(), memberInfo.getEmail(), member.getNickname(), memberInfo.getRole(), ACCESS_TTL);
        String refreshToken = jwtUtil.createJwt("refresh", memberInfo.getUserId(), memberInfo.getEmail(), member.getNickname(), memberInfo.getRole(), REFRESH_TTL);

        redisRefreshTokenService.createNewToken(member.getEmail(), refreshToken);

        return ResponseEntity.ok(new LoginTokenDto.Response(accessToken, refreshToken));
    }

}
