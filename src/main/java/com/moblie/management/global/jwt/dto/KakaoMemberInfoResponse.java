package com.moblie.management.global.jwt.dto;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class KakaoMemberInfoResponse implements OAuth2Response {

    private final Map<String, Object> attributes;
    private final Map<String, Object> kakaoAccountAttributes;
    private final Map<String, Object> propertiesAttributes;

    public KakaoMemberInfoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.kakaoAccountAttributes = (Map<String, Object>) attributes.get("kakao_account");
        this.propertiesAttributes = (Map<String, Object>) attributes.get("properties");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return kakaoAccountAttributes.get("email").toString();
    }

    @Override
    public String getName() {
        return propertiesAttributes.get("nickname").toString();
    }
}
