package com.moblie.management.redis;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

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
    public RedisTemplate<String, String> redisTemplate(RedissonClient redissonClient) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setConnectionFactory(new RedissonConnectionFactory(redissonClient));
        return template;
    }
}
