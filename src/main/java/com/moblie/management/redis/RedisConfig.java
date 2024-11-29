package com.moblie.management.redis;

//import io.lettuce.core.SocketOptions;
//import io.lettuce.core.cluster.ClusterClientOptions;
//import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ClusterOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;

//@Configuration
//@EnableRedisRepositories
//@RequiredArgsConstructor
//public class RedisConfig {
//
//    private final RedisClusterProperties redisClusterProperties;
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        final List<String> nodes = redisClusterProperties.getNodes();
//        final int maxRedirects = redisClusterProperties.getMaxRedirects();
//
//        final List<RedisNode> redisNodes = nodes.stream()
//                .map(node -> node.replace(",", ""))
//                .map(node -> {
//                    String[] parts = node.split(":");
//                    String host = parts[0];
//                    int port = Integer.parseInt(parts[1]);
//                    return new RedisNode(host, port);
//                })
//                .toList();
//
//        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration();
//        clusterConfiguration.setClusterNodes(redisNodes);
//        clusterConfiguration.setMaxRedirects(maxRedirects);
//
//        // 연결 시간 제한 늘리기
//        SocketOptions socketOptions = SocketOptions.builder()
//                .connectTimeout(Duration.ofMillis(5000L))  // 연결 시간 제한 5초로 설정
//                .keepAlive(true)
//                .build();
//
//        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
//                .dynamicRefreshSources(true)
//                .enableAllAdaptiveRefreshTriggers()
//                .enablePeriodicRefresh(Duration.ofMinutes(30L))  // 30분마다 토폴로지 새로고침
//                .build();
//
//        ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
//                .topologyRefreshOptions(clusterTopologyRefreshOptions)
//                .socketOptions(socketOptions)
//                .build();
//
//        LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
//                .clientOptions(clusterClientOptions)
//                .commandTimeout(Duration.ofMillis(5000L))  // 명령어 시간 제한 5초로 설정
//                .build();
//
//        return new LettuceConnectionFactory(clusterConfiguration, clientConfiguration);
//    }
//}

