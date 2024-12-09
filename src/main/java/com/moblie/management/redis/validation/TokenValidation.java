package com.moblie.management.redis.validation;

import com.moblie.management.exception.CustomException;
import com.moblie.management.exception.ErrorCode;

public class TokenValidation {

    public static void checkRefreshToken(String email) {
        if (email == null) {
            throw new CustomException(ErrorCode.ERROR_400, "유효하지 않은 토큰 값입니다.");
        }
    }

    public static void checkRefreshTokenType(String category) {
        if (!category.equals("refresh")) {
            throw new CustomException(ErrorCode.ERROR_400, "유효하지 않은 토큰 값입니다.");
        }
    }
}
