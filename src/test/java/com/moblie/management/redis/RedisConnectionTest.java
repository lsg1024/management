package com.moblie.management.redis;

import org.junit.jupiter.api.Test;
//import org.redisson.api.RBucket;
//import org.redisson.api.RedissonClient;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisConnectionTest {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void testRedisConnection() {
        String key = "{user}:test-key";
        String value = "test-value";

        redisTemplate.opsForValue().set(key, value);
        String result = redisTemplate.opsForValue().get(key);

        assertThat(result).isEqualTo(value);
    }

    @Test
    void testRedisKeyDelete() {
        String key = "{user}:test-key";

        redisTemplate.delete(key);
    }

    @Test
    void testAddKeyToRedis() {
        String key = "sample-key";
        String value = "sample-value";

        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value);
        String storedValue = bucket.get();

        assertThat(storedValue).isEqualTo(value);
    }
    @Test
    void testAddKeysToRedis() {

        String userKey = "user123:cart";
        RBucket<String> bucket = redissonClient.getBucket(userKey);
        bucket.set("item1, item2, item3");
        String cartItems = bucket.get();

        assertThat(cartItems).isEqualTo("item1, item2, item3");
    }

    @Test
    void testDeleteKeyFromRedis() {
        String key = "sample-key";

        // 키를 삭제합니다.
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.delete();
        String storedValue = bucket.get();

        // 삭제 후 값이 없는지 검증합니다.
        assertThat(storedValue).isNull();
    }

}
