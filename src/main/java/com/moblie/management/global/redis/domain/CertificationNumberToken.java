package com.moblie.management.global.redis.domain;

import org.springframework.data.annotation.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Getter @Setter
@RedisHash(value = "certification", timeToLive = 5 * 60)
public class CertificationNumberToken {
    @Id
    private String email;
    private String randomValue;
}
