package com.evoucher.evoucherbe.service.typed;

import com.evoucher.evoucherbe.common.enums.ExternalPinStatus;
import com.evoucher.evoucherbe.entity.ExternalPin;
import com.evoucher.evoucherbe.exception.CustomCodeException;
import com.evoucher.evoucherbe.service.ExternalPinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LockingService {
    @Value(value = "${redis.key.buy-choice.prefix}")
    private String choicePurchasingPrefix;

    private final RedissonClient redClient;

    private final RLock rExtPinLock;
    private final RMap<Long, List<Long>> rProcessingPins;


    private final ExternalPinService extPinService;
    public List<ExternalPin> getAvailablePins(Long goodId, ExternalPinStatus status, Integer numberPin, Date expireDate) {
        try {
            //lock
            log.info("lock");
            rExtPinLock.lock();

            // get all processing pins of current good
            List<Long> processingPins = rProcessingPins.getOrDefault(goodId, new ArrayList<>());

            log.info("pin being processed of good-{}: {}", goodId, processingPins);
            List<ExternalPin> result;
            if (processingPins.isEmpty()) {
                log.info("");
                result = extPinService.getOldestExtPinByNumberAndGoodsIdAndStatus(goodId, expireDate, status, numberPin);
            } else {
                result = extPinService.getOldestExtPinByNumberAndGoodsIdAndStatusAndIdNotIn(
                        processingPins, goodId, expireDate, status, numberPin);
            }

            List<Long> gotPins = result.stream().map(ExternalPin::getId).collect(Collectors.toList());
            log.info("got pin: {}", gotPins);
            if (CollectionUtils.isEmpty(result)) {
                return new ArrayList<>();
            }

            processingPins.addAll(gotPins);
            rProcessingPins.put(goodId, processingPins);
            log.info("mark {} as being processed pins", gotPins);
            return result;
        } finally {
            log.info("try unlock");
            rExtPinLock.unlock();
        }
    }

    public void removePrcDonePins(Long goodId, List<Long> pinIds) {
        rExtPinLock.lock();
        try {
            log.info("process done pins of good-{}: {}", goodId, pinIds);
            List<Long> cur = rProcessingPins.getOrDefault(goodId, new ArrayList<>());
            cur.removeAll(pinIds);
            if (cur.isEmpty()) {
                log.info("all pin of good-{} is done", goodId);
                rProcessingPins.remove(goodId);
            } else {
                rProcessingPins.put(goodId, cur);
            }
        } finally {
            rExtPinLock.unlock();
        }
    }

    public void lockPurchaseChoiceVoucherProcessor(String ev) throws CustomCodeException {
        log.info("lock purchasing: {}", ev);
        getPurchaseChoiceLocker(ev).lock();
    }

    public void unlockPurchaseChoiceVoucherProcessor(String ev) {
        log.info("release choice voucher purchasing: {}", ev);
        getPurchaseChoiceLocker(ev).unlock();
    }

    public RLock getPurchaseChoiceLocker(String ev) {
        log.info("get lock-{}", choicePurchasingPrefix.concat(ev));
        return redClient.getLock(choicePurchasingPrefix.concat(ev));
    }
}
