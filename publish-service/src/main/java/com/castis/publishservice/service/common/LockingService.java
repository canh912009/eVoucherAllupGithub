package com.castis.publishservice.service.common;

import com.castis.publishservice.entity.ExtPin;
import com.castis.publishservice.service.ExtPinService;
import com.castis.publishservice.utils.enum_template.ExtPinStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
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
    private final RLock rExtPinLock;
    private final RMap<Long, List<Long>> rProcessingPins;

    private final ExtPinService extPinService;

    public List<ExtPin> getPins(Long goodId, ExtPinStatus status, Integer numberOfPins, Date expireDate) {
        try {
            log.info("Get pins for product={}, status={}, number of pins={}, expireDate={}", goodId, status, numberOfPins, expireDate);
            //lock
            rExtPinLock.lock();

            // get all processing pins of current good
            List<Long> processingPins = rProcessingPins.getOrDefault(goodId, new ArrayList<>());

            log.info("Product={} has locked pins={}", goodId, processingPins);
            List<ExtPin> result;
            if (processingPins.isEmpty()) {
                log.info("No pin is locked for product={}. Retrieve {} pins order by date", goodId, numberOfPins);
                result = extPinService.getOldestExtPinByNumberAndGoodsIdAndStatus(goodId, expireDate, status, numberOfPins);
            } else {
                log.info("Pin is locked for product={}, retrieve {} pins order by date, except for={}",
                        goodId, numberOfPins, processingPins);
                result = extPinService.getOldestExtPinByNumberAndGoodsIdAndStatusAndIdNotIn(
                        processingPins, goodId, expireDate, status, numberOfPins);
            }

            List<Long> pinIds = result.stream().map(ExtPin::getId).collect(Collectors.toList());
            log.info("Found pins={}", pinIds);
            if (CollectionUtils.isEmpty(result)) {
                log.warn("No pin found for product={}", goodId);
                return new ArrayList<>();
            }
            result.forEach(o -> processingPins.add(o.getId()));
            //save to redis
            rProcessingPins.put(goodId, processingPins);
            log.info("Lock pins={}", pinIds);
            return new ArrayList<>(result);
        } finally {
            rExtPinLock.unlock();
        }
    }

    public void removePrcDonePins(Long goodId, List<Long> pinIds) {
        log.info("Release pins={} for product={}", pinIds, goodId);
        rExtPinLock.lock();
        try {
            List<Long> currentLockedPins = rProcessingPins.getOrDefault(goodId, new ArrayList<>());
            currentLockedPins.removeAll(pinIds);
            log.info("Released pins={} for product={}", pinIds, goodId);
            if (currentLockedPins.isEmpty()) {
                log.info("No more pin is locked for product={}", goodId);
                rProcessingPins.remove(goodId);
            } else {
                log.info("Some pins are still locked for product={}", goodId);
                rProcessingPins.put(goodId, currentLockedPins);
            }
        } finally {
            rExtPinLock.unlock();
        }
    }

}
