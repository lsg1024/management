package com.moblie.management.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.redisnode.RedisNode;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedissonConfig {

    private final RedisClusterProperties redisClusterProperties;

    @Bean
    public RedissonClient redissonClient() {
        final Config config = new Config();
        List<String> nodes = redisClusterProperties.getNodes();
        final List<String> redisNodes =  nodes.stream()
                .map(node -> node.replace(",", ""))
                .map(String::trim)
                .toList();

        ClusterServersConfig clusterServersConfig = config.useClusterServers()
                .setScanInterval(2000)
                .setConnectTimeout(500)
                .setTimeout(3000)
                .setRetryAttempts(3)
                .setRetryInterval(1500);

        redisNodes.forEach(node ->
                clusterServersConfig.addNodeAddress("redis://" + node));

        return Redisson.create(config);
    }


}
