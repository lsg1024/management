package com.moblie.management.global.jwt.handler;

import com.moblie.management.global.jwt.dto.PrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final LoginUtil loginUtil;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        PrincipalDetails customUserDetails = (PrincipalDetails) authentication.getPrincipal();
        loginUtil.handleSuccessfulAuthentication(customUserDetails.getId(), customUserDetails.getEmail(), customUserDetails.getUsername(), customUserDetails.getRole(), response);

    }

}

