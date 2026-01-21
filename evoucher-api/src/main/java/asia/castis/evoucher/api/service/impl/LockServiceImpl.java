package asia.castis.evoucher.api.service.impl;

import asia.castis.evoucher.api.service.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LockServiceImpl implements LockService {
    private final RedissonClient redClient;
    @Value("${redis.lock.choice:CHOOSE_CHOICE_}")
    private String purchasingPrefix;
    @Override
    public RLock getPurchaseChoiceLocker(String ev) {
        log.info("Get voucher lock ev={}", purchasingPrefix.concat(ev));
        return redClient.getLock(purchasingPrefix.concat(ev));
    }
}
