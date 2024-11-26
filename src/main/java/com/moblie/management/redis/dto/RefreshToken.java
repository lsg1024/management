package com.moblie.management.redis.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter @Setter
@RedisHash(value = "refreshToken", timeToLive = 259200)
public class RefreshToken {
    @Id
    private String series;
    private String username;
    private String tokenValue;
    private Long lastUsed;

}
