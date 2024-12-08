package com.moblie.management.redis.domain;

import org.springframework.data.annotation.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Getter @Setter
@RedisHash(value = "passwordToken", timeToLive = 3 * 60)
public class CertificationNumberToken {
    @Id
    private String username;
    private String randomValue;
    private String password;
}
