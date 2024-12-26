package com.moblie.management.global.redis;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class RedissonConfig {

    private final RedisClusterProperties redisClusterProperties;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // Redis 클러스터 설정
        String[] nodeAddresses = redisClusterProperties.getNodes().stream()
                .map(node -> "redis://" + node.replace(",", ""))
                .toArray(String[]::new);

        config.useClusterServers()
                .addNodeAddress(nodeAddresses)
                .setScanInterval(2000)  // 클러스터 스캔 주기 설정 (밀리초)
                .setConnectTimeout(5000)  // 연결 시간 제한 (밀리초)
                .setIdleConnectionTimeout(10000);  // 유휴 연결 시간 제한 (밀리초)

        return Redisson.create(config);
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory cf) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofMinutes(3L)); // 캐시 수명 설정

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(cf)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedissonClient redissonClient) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(new RedissonConnectionFactory(redissonClient));
        return template;
    }
}
