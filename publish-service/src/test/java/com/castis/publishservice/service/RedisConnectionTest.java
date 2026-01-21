package com.castis.publishservice.service;

import com.castis.publishservice.config.RedisConfig;
import com.castis.publishservice.repository.ExtPinRepository;
import com.castis.publishservice.repository.GoodRepository;
import com.castis.publishservice.service.common.LockingService;
import com.castis.publishservice.service.external_pin.ExtServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest(classes = {RedisConfig.class, LockingService.class})
@Slf4j
public class RedisConnectionTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedissonClient extPinClient;
    @Autowired
    private LockingService lockingService;
    @Autowired
    private RMap<Long, List<Long>> rTestMap;
    @MockBean
    private ExtPinService extPinService;
    @MockBean
    private  ExtPinRepository repository;
    @MockBean
    private  GoodsService goodsService;
    @MockBean
    private  ExtServiceFactory extServiceFactory;

    @BeforeEach
    void setup() {
//        lockingService.setRTestMap(rTestMap);
    }
    @Test
    public void testRedisConnection() {
        // Assert that the RedisTemplate is not null
        assertNotNull(redisTemplate);

        // Perform a simple Redis operation
        String key = "testKey";
        String value = "testValue";

        redisTemplate.opsForValue().set(key, value);
        String retrievedValue = (String) redisTemplate.opsForValue().get(key);

        // Assert the value was correctly set and retrieved
        assertEquals(value, retrievedValue);
    }

    @Test
    public void redissonClient_Test() {
        assertNotNull(extPinClient);

        RMap<String, List<Long>> map = extPinClient.getMap("EXT_PIN_MAP");
        map.remove("test");

        List<Long> initial = List.of(0L, 1L);
        map.put("test", initial);

        List<Long> list = new  ArrayList<>(map.get("test"));
        list.add(3L);

        map.put("test", list);

        List<Long> resultList = map.get("test");

        Assertions.assertThat(resultList).isNotNull().hasSize(3);
    }


}
