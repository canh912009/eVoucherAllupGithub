package com.castis.publishservice.service.external_pin;

import com.castis.publishservice.dto.GoodsDTO;
import com.castis.publishservice.entity.ExtPin;
import com.castis.publishservice.exception.defineException.ServerRuntimeException;
import com.castis.publishservice.service.ExtPinService;
import com.castis.publishservice.service.common.LockingService;
import com.castis.publishservice.utils.enum_template.ExtPinStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("EXTERNAL")
@RequiredArgsConstructor
@Slf4j
public class ExtTypeService implements IntegratedPinService {

    private final ExtPinService pinService;
    private final LockingService lockingService;

    @Override
    public void reservePin(Integer numberOfUsers, GoodsDTO goods, Date bookingDate) {

        log.info("get all available pin for {} users", numberOfUsers);
        List<ExtPin> extPins = lockingService.getPins(goods.getId(), ExtPinStatus.AVAILABLE, numberOfUsers, bookingDate);
        try {
            if (extPins.size() < numberOfUsers) {
                log.error("don't have enough external pin for all customer. found: {} < {}", extPins.size(), numberOfUsers);
                lockingService.removePrcDonePins(goods.getId(), extPins.stream().map(ExtPin::getId).collect(Collectors.toList()));
                throw new ServerRuntimeException("don't have enough external pin for all customer.");
            }
            log.info("change pin status to reserved");
            extPins.forEach(o -> o.setStatus(ExtPinStatus.RESERVED));
            pinService.updateExtPins(extPins);
            log.info("reserved all external pin: {}", extPins);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            lockingService.removePrcDonePins(goods.getId(), extPins.stream().map(ExtPin::getId).collect(Collectors.toList()));
        }
    }

    @Override
    public List<ExtPin> getAvailablePin(int totalSize, GoodsDTO goods, Date bookingDate) {
        log.info("Get {} AVAILABLE pins of good={} with expired date > {}", totalSize, goods.getId(), bookingDate);
        List<ExtPin> extPins = lockingService.getPins(goods.getId(), ExtPinStatus.AVAILABLE, totalSize, bookingDate);

        if (extPins.size() < totalSize) {
            String message = "Not enough external pin for all customer. missing " + (totalSize - extPins.size()) + " pins";
            log.error(message);
            lockingService.removePrcDonePins(goods.getId(), extPins.stream().map(ExtPin::getId).collect(Collectors.toList()));
            throw new ServerRuntimeException(message);
        }
        return extPins;
    }

    @Override
    public List<ExtPin> getReservedPin(int totalSize, GoodsDTO goods, Date bookingDate) {
        log.info("Get {} RESERVED pins of good={} with expired date > {}", totalSize, goods.getId(), bookingDate);
        List<ExtPin> extPins = lockingService.getPins(goods.getId(), ExtPinStatus.RESERVED, totalSize, bookingDate);

        if (extPins.size() < totalSize) {
            String message = "Not enough external pin for all customer. missing " + (totalSize - extPins.size()) + " pins";
            log.warn(message);
            log.info("get available pin for remaining customer: ");

            List<ExtPin> additionPins = lockingService.getPins(goods.getId(), ExtPinStatus.AVAILABLE, totalSize - extPins.size(), bookingDate);
            extPins.addAll(additionPins);
            if (extPins.size() < totalSize) {
                message += ", even request additional pins is not enough";
                log.error(message);
                lockingService.removePrcDonePins(goods.getId(), extPins.stream().map(ExtPin::getId).collect(Collectors.toList()));
                throw new ServerRuntimeException(message);
            }


        }
        return extPins;
    }
}
