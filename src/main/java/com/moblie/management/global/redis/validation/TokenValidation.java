package com.moblie.management.global.redis.validation;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;

public class TokenValidation {

    public static void validateRefreshToken(String email) {
        if (email == null) {
            throw new CustomException(ErrorCode.ERROR_404, "유효하지 않은 토큰 값입니다.");
        }
    }

    public static void validateRefreshTokenType(String category) {
        if (!category.equals("refresh")) {
            throw new CustomException(ErrorCode.ERROR_404, "유효하지 않은 토큰 값입니다.");
        }
    }

    public static void validateCertificationNumber(String redisCertificationToken, String certificationNumber) {
        if (!redisCertificationToken.equals(certificationNumber)) {
            throw new CustomException(ErrorCode.ERROR_404, "유효시간 만료");
        }
    }

}
