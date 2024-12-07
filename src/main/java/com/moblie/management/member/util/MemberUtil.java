package com.moblie.management.member.util;

import jakarta.servlet.http.Cookie;

public class MemberUtil {

    public static Cookie createCookie(String key, String value, int TTL) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(TTL);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

}
