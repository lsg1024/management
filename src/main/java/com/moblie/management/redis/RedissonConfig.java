package com.moblie.management.redis;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.ClusterServersConfig;
//import org.redisson.config.Config;
//import org.redisson.redisnode.RedisNode;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableRedisRepositories
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
    public RedisTemplate<String, Object> redisTemplate(RedissonClient redissonClient) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // RedissonClient를 사용하여 RedisConnectionFactory 설정
        template.setConnectionFactory(new RedissonConnectionFactory(redissonClient));
        return template;
    }
}
