package com.moblie.management.member.validation;

import com.moblie.management.exception.CustomException;
import com.moblie.management.exception.ErrorCode;
import com.moblie.management.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberValidation {

    private final MemberRepository memberRepository;

    public static void checkConfirmPassword(String password, String confirm_password) {
        if (!password.equals(confirm_password)) {
            throw new CustomException(ErrorCode.REQUEST_FAILED, "비밀번호가 일치하지 않습니다.");
        }
    }

    public static void checkEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new CustomException(ErrorCode.REQUEST_FAILED, "옳바른 사용자 정보를 입력해주세요");
        }
    }

    public static void checkPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new CustomException(ErrorCode.REQUEST_FAILED, "옳바른 사용자 정보를 입력해주세요");
        }
    }

}
