package com.moblie.management.redis;

//import io.lettuce.core.SocketOptions;
//import io.lettuce.core.cluster.ClusterClientOptions;
//import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
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
//        final List<String> nodes = redisClusterProperties.getNode();
//        final int maxRedirects = redisClusterProperties.getMaxRedirects();
//        final List<RedisNode> redisNodes = nodes.stream()
//                .map(node -> new RedisNode(node.split(":")[0], Integer.parseInt(node.split(":")[1])))
//                .toList();
//
//        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration();
//        clusterConfiguration.setClusterNodes(redisNodes);
//        clusterConfiguration.setMaxRedirects(maxRedirects);
//
//        SocketOptions socketOptions = SocketOptions.builder()
//                .connectTimeout(Duration.ofMillis(100L))
//                .keepAlive(true)
//                .build();
//
//        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
//                .dynamicRefreshSources(true)
//                .enableAllAdaptiveRefreshTriggers()
//                .enablePeriodicRefresh(Duration.ofMillis(30L))
//                .build();
//
//        ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
//                .topologyRefreshOptions(clusterTopologyRefreshOptions)
//                .socketOptions(socketOptions)
//                .build();
//
//        // (5) Lettuce Client 옵션
//        LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
//                .clientOptions(clusterClientOptions)
//                .commandTimeout(Duration.ofMillis(3000L))
//                .build();
//
//        return new LettuceConnectionFactory(clusterConfiguration, clientConfiguration);
//    }
//}

