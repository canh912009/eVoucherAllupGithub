package com.evoucher.adminapi.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@RequiredArgsConstructor
public class RedisConfiguration {

    private final RedisProperties redisProperties;
    @Value("${spring.redis.standalone}")
    private boolean isStandAloneRedis;


    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        if (isStandAloneRedis) {
            return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
        } else {
            RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration()
                    .clusterNode(redisProperties.getHost(), redisProperties.getPort());

            return new LettuceConnectionFactory(clusterConfig);
        }
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}