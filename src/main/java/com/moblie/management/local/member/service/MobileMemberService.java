package com.moblie.management.local.member.service;

import com.moblie.management.global.jwt.JwtUtil;
import com.moblie.management.global.jwt.dto.KakaoMemberInfoResponse;
import com.moblie.management.global.redis.service.RedisRefreshTokenService;
import com.moblie.management.local.member.dto.LoginTokenDto;
import com.moblie.management.local.member.dto.MemberDto;
import com.moblie.management.local.member.model.MemberEntity;
import com.moblie.management.local.member.model.Role;
import com.moblie.management.local.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MobileMemberService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final RedisRefreshTokenService redisRefreshTokenService;

    public KakaoMemberInfoResponse kakaoApi(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = new RestTemplate().exchange(url, HttpMethod.GET, request, Map.class);

        return new KakaoMemberInfoResponse(response.getBody());
    }

//    public LoginTokenDto.Response login(LoginTokenDto.Request request) {
//
//        String username = request.getProvider() + " " + request.getProviderId();
//        String email = request.getEmail();
//        String nickname = request.getNickname();
//
//        MemberEntity existMember = memberRepository.findByUsernameAndEmail(username, email);
//
//        if (existMember == null) {
//            MemberEntity newMember = MemberEntity.builder()
//                    .username(username)
//                    .email(email)
//                    .nickname(nickname)
//                    .role(Role.USER)
//                    .build();
//
//            existMember = memberRepository.save(newMember);
//        } else {
//            existMember.updateNicknameAndEmail(nickname, request.getEmail());
//        }
//
//        MemberDto.MemberInfo memberInfo = existMember.getMemberInfo();
//        String accessToken = jwtUtil.createJwt("access",
//                memberInfo.getUserId(),
//                memberInfo.getEmail(),
//                memberInfo.getRole(),
//                ACCESS_TTL);
//
//        String refreshToken = jwtUtil.createJwt("refresh",
//                memberInfo.getUserId(),
//                memberInfo.getEmail(),
//                memberInfo.getRole(),
//                REFRESH_TTL);
//
//        // 리프레시 토큰 DB 저장
//        redisRefreshTokenService.createNewToken(memberInfo.getEmail(), refreshToken);
//
//        return new LoginTokenDto.Response(accessToken, refreshToken);
//
//    }

}
