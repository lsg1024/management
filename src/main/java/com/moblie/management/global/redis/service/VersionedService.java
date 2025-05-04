package com.moblie.management.global.redis.service;

import lombok.val;
import org.springframework.data.redis.core.RedisTemplate;

public abstract class VersionedService {

    protected abstract RedisTemplate<String, Integer> getVersionTemplate();
    protected abstract String getVersionKey();

    protected int increaseVersion() {
        Long newVersion = getVersionTemplate().opsForValue().increment(getVersionKey());
        return newVersion != null ? newVersion.intValue() : 0;
    }

    public int getCurrentVersion() {
        Integer version = getVersionTemplate().opsForValue().get(getVersionKey());
        return version != null ? version : 0;
    }
}
