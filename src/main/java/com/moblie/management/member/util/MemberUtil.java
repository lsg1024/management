package com.moblie.management.member.util;

import jakarta.servlet.http.Cookie;

import java.util.Random;

public class MemberUtil {

    public static Cookie createCookie(String key, String value, int TTL) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(TTL);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    public static String randomNumbers(Random random) {
        StringBuilder randomNumber = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            randomNumber.append(random.nextInt(10));
        }
        return randomNumber.toString();
    }

}
