package com.moblie.management.global.jwt.service;

import com.moblie.management.global.jwt.dto.KakaoMemberInfoResponse;
import com.moblie.management.global.jwt.dto.OAuth2Response;
import com.moblie.management.global.jwt.dto.PrincipalDetails;
import com.moblie.management.local.member.model.MemberEntity;
import com.moblie.management.local.member.model.Role;
import com.moblie.management.local.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oAuth2User Data = {}" ,oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response;

        if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoMemberInfoResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        MemberEntity member = processOAuth2User(oAuth2Response);
        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }

    public MemberEntity processOAuth2User(OAuth2Response oAuth2Response) {
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        String email = oAuth2Response.getEmail();
        String nickname = oAuth2Response.getName();

        MemberEntity member = memberRepository.findByUsernameAndEmail(username, email);

        if (member == null) {
            log.info("신규 회원 가입 처리");
            member = MemberEntity.builder()
                    .username(username)
                    .email(email)
                    .nickname(nickname)
                    .role(Role.USER)
                    .build();
            return memberRepository.save(member);
        }

        log.info("기존 회원 업데이트 처리");
        member.updateNicknameAndEmail(nickname, email);
        return memberRepository.save(member);
    }

}
