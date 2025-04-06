package com.moblie.management.local.member.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

public class MemberDto {

    private final static String EMAIL_ERROR = "이메일 형식이 올바르지 않습니다.";
    private final static String PASSWORD_ERROR = "비밀번호는 영문자, 숫자, 특수문자를 포함한 8~16자리여야 합니다.";

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUp {

        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = EMAIL_ERROR)
        private String email;

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@!%*#?&]).{8,16}$",
                message = PASSWORD_ERROR)
        private String password;

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@!%*#?&]).{8,16}$",
                message = PASSWORD_ERROR)
        private String password_confirm;

        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$",
                message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
        private String nickname;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignIn {
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$",
                message = EMAIL_ERROR)
        private String email;

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@!%*#?&]).{8,16}$",
                message = PASSWORD_ERROR)
        private String password;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberEmail {
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$",
                message = EMAIL_ERROR)
        private String email;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdatePassword {
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@!%*#?&]).{8,16}$",
                message = PASSWORD_ERROR)
        private String password;
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@!%*#?&]).{8,16}$",
                message = PASSWORD_ERROR)
        private String password_confirm;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteMember {
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@!%*#?&]).{8,16}$",
                message = PASSWORD_ERROR)
        private String password;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberInfo {
        private String userId;
        private String email;
        private String role;
    }

}
