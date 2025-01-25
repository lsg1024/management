package com.moblie.management.global.redis.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter @Setter
@RedisHash(value = "idempotency", timeToLive = 60)
public class IdempotencyToken {

    @Id
    private String id;
    private String idempotencyValue;
}
