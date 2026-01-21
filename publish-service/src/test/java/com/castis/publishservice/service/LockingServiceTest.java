package com.castis.publishservice.service;

import com.castis.publishservice.service.common.LockingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class LockingServiceTest {
    @InjectMocks
    private LockingService service;

    @Mock
    private RLock rLock;
    @Mock
    private RedissonClient redissonClient;
    @Mock
    private RLock rExtPinLock;
    @Mock
    private RMap<Long, List<Long>> rProcessingPins;
    @Mock
    private ExtPinService extPinService;


    @BeforeEach
    public void setUp() {
//        when(redissonClient.getLock(anyString())).thenReturn(rLock);
    }

    @Test
    public void twoThreadGetAvailablePinAtSameTime_thenReturnDifferentPins() {
        HashMap<Long, List<Long>> processingMap = new HashMap<>();

    }

}
