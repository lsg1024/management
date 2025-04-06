package com.moblie.management.local.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class LoginTokenDto {

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "accessToken 누락")
        private String accessToken;

    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private String accessToken;
        private String refreshToken;
    }

}
