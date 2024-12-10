package com.moblie.management.member.validation;

import com.moblie.management.exception.CustomException;
import com.moblie.management.exception.ErrorCode;
import com.moblie.management.member.domain.MemberEntity;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class MemberValidation {
    public static void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new CustomException(ErrorCode.ERROR_400, "옳바른 사용자 정보를 입력해주세요");
        }
    }

    public static void validateConfirmPassword(String password, String confirm_password) {
        if (!password.equals(confirm_password)) {
            throw new CustomException(ErrorCode.ERROR_400, "비밀번호가 일치하지 않습니다.");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new CustomException(ErrorCode.ERROR_400, "옳바른 사용자 정보를 입력해주세요");
        }
    }

}
