package com.moblie.management.local.member.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

public class AdminDto {

    private final static String EMAIL_ERROR = "이메일 형식이 올바르지 않습니다.";

    @Getter
    public static class UpdateMemberInfo {
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = EMAIL_ERROR)
        private String email;
    }

}
