package com.castis.publishservice.config;

import io.lettuce.core.RedisClient;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Configuration
public class RedisConfig {
    @Value("${redis.host:192.168.0.125}")
    private String redisHost;

    @Value("${redis.port:6379}")
    private int redisPort;

    @Value("${redis.isStandAlone:true}")
    private boolean isStandAloneRedis;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisConfiguration redisConfig;
        if (isStandAloneRedis) {
            redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        } else {
            redisConfig = new RedisClusterConfiguration().clusterNode(redisHost, redisPort);
        }
        return new LettuceConnectionFactory(redisConfig);
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    Config redissionConfig() {
        Config config = new Config();
        if (isStandAloneRedis) {
            config.useSingleServer()
                    .setAddress(String.format("redis://%s:%d", redisHost, redisPort));
        } else {
            config.useClusterServers().setNodeAddresses(List.of(String.format("redis://%s:%d", redisHost, redisPort)));
        }
        return config;
    }

    @Bean("extPinClient")
    RedissonClient extPinClient() {
        return Redisson.create(redissionConfig());
    }

    @Bean("rExtPinLock")
    RLock extPinLock() {
        return extPinClient().getLock("EXT_PIN_LOCK");
    }

    @Bean("rProcessingPins")
    RMap<Long, List<Long>> processingPins() {
        return extPinClient().getMap("PROCESSING_PIN");
    }


}