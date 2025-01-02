package com.moblie.management.local.member.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

    WAIT("WAIT", "미승인 유저"),
    USER("USER", "승인 유저"),
    ADMIN("ADMIN", "관리자");

    private final String key;
    private final String title;

}
