package com.evoucher.adminapi.service;

import com.evoucher.evoucherbe.common.enums.ExternalPinStatus;
import com.evoucher.evoucherbe.config.RedisConfig;
import com.evoucher.evoucherbe.entity.ExternalPin;
import com.evoucher.evoucherbe.exception.CustomCodeException;
import com.evoucher.evoucherbe.service.ExternalPinService;
import com.evoucher.evoucherbe.service.typed.LockingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@Slf4j
@SpringBootTest(classes = {RedisConfig.class, LockingService.class})
public class LockingServiceTest {
    @Autowired
    @InjectMocks
    private LockingService lockingService;

    @Autowired
    private RLock rExtPinLock;
    @Autowired
    private RedissonClient redClient;
    @MockBean
    private RMap<Long, List<Long>> rProcessingPins;
    @MockBean
    private ExternalPinService extPinService;

    private final HashMap<Long, List<Long>> fakeProcessingPins = new HashMap<>();
    private final Map<Long, List<ExternalPin>> pinMap = Map.of(
            1L, List.of(
                    ExternalPin.builder()
                            .id(1L)
                            .goodsId(1L)
                            .build(),
                    ExternalPin.builder()
                            .id(2L)
                            .goodsId(1L)
                            .build(),
                    ExternalPin.builder()
                            .id(3L)
                            .goodsId(1L)
                            .build()
            )
    );


    @BeforeEach
    public void setup() {
        when(rProcessingPins.getOrDefault(anyLong(), anyList()))
                .thenAnswer(invocationOnMock -> {
                    Long param = invocationOnMock.getArgument(0);
                    return fakeProcessingPins.getOrDefault(param, new ArrayList<>());
                });

        when(rProcessingPins.put(anyLong(), anyList()))
                .thenAnswer(invocationOnMock -> {
                    Long key = invocationOnMock.getArgument(0);
                    List<Long> valueList = invocationOnMock.getArgument(1);
                    fakeProcessingPins.put(key, valueList);
                    return valueList;
                });

        when(extPinService.getOldestExtPinByNumberAndGoodsIdAndStatus(anyLong(), any(), eq(ExternalPinStatus.AVAILABLE), anyInt()))
                .thenAnswer(invocationOnMock -> {
                    Long goodId = invocationOnMock.getArgument(0);
                    Integer count = invocationOnMock.getArgument(3);
                    return pinMap.get(goodId).subList(0, count);
                });
        when(extPinService.getOldestExtPinByNumberAndGoodsIdAndStatusAndIdNotIn(anyList(), anyLong(), any(), eq(ExternalPinStatus.AVAILABLE), anyInt()))
                .thenAnswer(invocationOnMock -> {
                    List<Long> exist = invocationOnMock.getArgument(0);
                    Long goodId = invocationOnMock.getArgument(1);
                    Integer count = invocationOnMock.getArgument(4);
                    return pinMap.get(goodId).stream().filter(o -> !exist.contains(o.getId())).collect(Collectors.toList()).subList(0, count);
                });
    }

    @Test
    public void twoThreadGetAvailablePinAtSameTime_thenReturnDifferentPins() {

        try {
            CountDownLatch latch = new CountDownLatch(2);
            ExecutorService executor = Executors.newFixedThreadPool(2);

            AtomicReference<List<ExternalPin>> result1 = new AtomicReference<>(), result2 = new AtomicReference<>() ;

            Runnable task = () -> {
                try {
                    result1.set(lockingService.getAvailablePins(1L, ExternalPinStatus.AVAILABLE, 1, new Date()));
                    log.info("task1: {}", result1.get());
                } finally {
                    latch.countDown();
                }
            };
            Runnable task2 = () -> {
                try {
                    result2.set(lockingService.getAvailablePins(1L, ExternalPinStatus.AVAILABLE, 2, new Date()));
                    log.info("task2: {}", result2.get());
                } finally {
                    latch.countDown();
                }
            };

            executor.submit(task);
            executor.submit(task2);

            latch.await();

            verify(extPinService, times(1)).getOldestExtPinByNumberAndGoodsIdAndStatus(anyLong(), any(), eq(ExternalPinStatus.AVAILABLE), anyInt());
            verify(extPinService, times(1)).getOldestExtPinByNumberAndGoodsIdAndStatusAndIdNotIn(anyList(), anyLong(), any(), eq(ExternalPinStatus.AVAILABLE), anyInt());
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }


    }


    @Test
    public void lockChoiceVoucherPurchasing_thenLockIfSameParentVoucherForMultipleRequest() throws InterruptedException, CustomCodeException {
        AtomicReference<Boolean> checkLock = new AtomicReference<>(false);
        CountDownLatch latch = new CountDownLatch(5);
        CountDownLatch latchStart = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(4);
        StopWatch watch = new StopWatch();
        CopyOnWriteArrayList<Long> waitingTime = new CopyOnWriteArrayList<>();
        Runnable task = getRunnable(watch, latch, "ev-1", waitingTime);
        Runnable task2 = getRunnable(watch, latch, "ev-2", waitingTime);
        Runnable task3 = getRunnable(watch, latch, "ev-1", waitingTime);
        Runnable task4 = getRunnable(watch, latch, "ev-1", waitingTime);

        Runnable statusTask = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                RLock lock = lockingService.getPurchaseChoiceLocker("ev-1");
                checkLock.set(lock.isLocked());
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            } finally {
                latch.countDown();
            }});

        executor.submit(task);
        executor.submit(task2);
        executor.submit(task3);
        executor.submit(task4);
        executor.submit(statusTask);

        latchStart.countDown();
        watch.start();
        latchStart.await();


        latch.await();
        Assertions.assertThat(waitingTime.size()).isEqualTo(4);
        List<Long> waitingList = new ArrayList<>(waitingTime);
        log.info("result : {}", waitingList);
        Assertions.assertThat((int) waitingList.stream().filter(o -> o < 6000).count()).isEqualTo(2);
        Assertions.assertThat((int) waitingList.stream().filter(o -> o > 6000).count()).isEqualTo(2);
        Assertions.assertThat((int) waitingList.stream().filter(o -> o > 9000).count()).isEqualTo(1);
        Assertions.assertThat(checkLock.get()).isEqualTo(true);
    }

    @NotNull
    private Runnable getRunnable(StopWatch watch, CountDownLatch latch, String key, CopyOnWriteArrayList<Long> waitingTime) throws CustomCodeException {
        return () -> {
            try {
                lockingService.lockPurchaseChoiceVoucherProcessor(key);
                log.info("job-1 run");
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                log.info("interrupt exception", e);
            } finally {
                waitingTime.add(watch.getTime());
                lockingService.unlockPurchaseChoiceVoucherProcessor(key);
                latch.countDown();
            }
        };
    }
}
