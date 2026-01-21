package com.castis.publishservice.service;

import com.castis.publishservice.dto.GoodsDTO;
import com.castis.publishservice.dto.request.ExternalPinGiftPopRequest;
import com.castis.publishservice.entity.ExtPin;
import com.castis.publishservice.entity.Goods;
import com.castis.publishservice.exception.defineException.CustomCodeException;
import com.castis.publishservice.exception.defineException.NotFoundException;
import com.castis.publishservice.exception.defineException.ServerRuntimeException;
import com.castis.publishservice.repository.ExtPinRepository;
import com.castis.publishservice.repository.GoodRepository;
import com.castis.publishservice.service.external_pin.ExtServiceFactory;
import com.castis.publishservice.service.external_pin.GiftPopService;
import com.castis.publishservice.service.external_pin.UrBoxService;
import com.castis.publishservice.utils.enum_template.ExtPinStatus;
import com.castis.publishservice.utils.enum_template.SystemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExtPinService {
    private final ExtPinRepository repository;

    private final GoodsService goodsService;
    private final ExtServiceFactory extServiceFactory;

    public Long countAllExtPinByGoodsIdAndStatus(Long goodsId, Date expireDate, ExtPinStatus status) {
        try {
            return repository.countAllByStatusAndGoodsIdAndExpireTimeAfter(status, goodsId, expireDate);
        } catch (Exception e) {
            log.error("Error when count all available ext pin");
            log.error(e.getMessage(), e);
            throw new ServerRuntimeException(e.getMessage(), e);
        }
    }
    public List<ExtPin> getOldestExtPinByNumberAndGoodsIdAndStatus(Long goodsId, Date expireDate, ExtPinStatus status, Integer number) {
        try {
            return repository.findByStatusAndGoodsIdAndExpireTimeAfterOrderByRegDtAsc(status, goodsId, expireDate, Pageable.ofSize(number)).getContent();
        } catch (Exception e) {
            log.error("error when get oldest available ext pin by number: " + number);
            log.error(e.getMessage(), e);
            throw new ServerRuntimeException(e.getMessage(), e);
        }
    }
    public List<ExtPin> getOldestExtPinByNumberAndGoodsIdAndStatusAndIdNotIn(List<Long> exceptId, Long goodsId, Date expireDate, ExtPinStatus status, Integer number) {
        if (number == 0) {
            log.error("getting pin number can not be 0");
            return new ArrayList<>();
        }
        try {
            return repository.findByIdNotInAndStatusAndGoodsIdAndExpireTimeAfterOrderByRegDtAsc(exceptId, status, goodsId, expireDate, Pageable.ofSize(number)).getContent();
        } catch (Exception e) {
            log.error("error when get oldest available ext pin by number: " + number);
            log.error(e.getMessage(), e);
            throw new ServerRuntimeException(e.getMessage(), e);
        }
    }
    public void updateExtPins(List<ExtPin> extPins) {
        try {
            repository.saveAll(extPins);
        } catch (Exception e) {
            log.error("get error when update ext pin");
            log.error(e.getMessage(), e);
            throw new ServerRuntimeException(e.getMessage(), e);
        }
    }

//    public List<ExtPin> getPinAvailableForGiftPopAndUpdatePinToReserved(
//            Long goodsId, Integer numberPin, Date expireDate,
//            SystemType type) {
//        try {
//            log.info("Find Goods with goodsId: {}", goodsId);
//            Goods goods = goodRepository.findById(goodsId)
//                    .orElseThrow(() -> new NotFoundException("Goods not found with id: " + goodsId));
//
//            log.info("Count pins with good id: {}, number: {}, expire date: {}",
//                    goodsId, numberPin, expireDate);
//
//            int countAvailablePin = countAllExtPinByGoodsIdAndStatus(
//                    goodsId, expireDate, ExtPinStatus.AVAILABLE).intValue();
//            log.info("The number of Pins remaining is: {}", countAvailablePin);
//
//            List<ExtPin> extPins = new ArrayList<>();
//            if (countAvailablePin >= numberPin) {
//                log.info("Returns the number of Pins taken from the Database with goodsId: {}", goodsId);
//                extPins = getAvailableExtPin(goodsId, ExtPinStatus.AVAILABLE, numberPin, expireDate);
//
//                return updateExternalPinStatusIsRESERVED(extPins);
//            }
//
//            if (countAvailablePin > 0) {
//                log.info("Get the number of Pins remaining in the Database with goodsId: {}", goodsId);
//                extPins.addAll(getAvailableExtPin(goodsId, ExtPinStatus.AVAILABLE, countAvailablePin, expireDate));
//            }
//
//            int numberPinMissing = numberPin - countAvailablePin;
//            log.info("Order more Pin from Gift Pop for goodsId: {} with number: {}",
//                    goods.getId(), numberPinMissing);
//            if (numberPinMissing > 0) {
//                List<ExtPin> orderedPins = new ArrayList<>();
//                if (SystemType.GIFTPOP.equals(type)) {
//                    log.info("call gift pop to order missing external pin");
////                    orderedPins = orderGiftPopPin(numberPinMissing, goods);
//                } else if (SystemType.UR_BOX.equals(type)) {
//                    log.info("call ur box to order missing external pins");
//
//                    int pinToOrder = numberPinMissing;
//                    while (pinToOrder > 0) {
//                        int orderSize = Math.min(pinToOrder, 30);
//                        log.info("call ur box to order {} missing external pins", orderSize);
//                        List<ExtPin> orderedPinsThisTime = urBoxService.orderUrboxPin(orderSize, goods);
//                        orderedPins.addAll(orderedPinsThisTime);
//                        pinToOrder -= orderSize;
//                    }
//                }
//                extPins.addAll(orderedPins);
//            }
//
//            extPins = updateExternalPinStatusIsRESERVED(extPins);
//
//            return extPins;
//        } catch (CustomCodeException e) {
//            log.error(e.getErrorCode(), e);
//            throw e;
//        } catch (Exception e) {
//            log.error("Error get pin for Gift Pop: {}", e.getMessage(), e);
//            throw new CustomCodeException(
//                    "Error get pin for Gift Pop with goodsId: " + goodsId,
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        } finally {
////            lock.unlock();
//        }
//    }

    private List<ExtPin> updateExternalPinStatusIsRESERVED(List<ExtPin> extPins) {
        log.info("Update external Pin status is RESERVED");
        if (CollectionUtils.isEmpty(extPins)) {
            throw new IllegalArgumentException("List External PIN is empty");
        }
        extPins.forEach(extPin -> extPin.setStatus(ExtPinStatus.RESERVED));
        return repository.saveAll(extPins);
    }

    public List<Long> getList3rdPartyExternalPin(ExternalPinGiftPopRequest request, SystemType type) {
        GoodsDTO goods = goodsService.findById(request.getGoodsId());
        List<ExtPin> extPins = extServiceFactory.getServiceByType(type).getAvailablePin(
                request.getQuantity(),
                goods,
                request.getExpireDate()
        );
        extPins = updateExternalPinStatusIsRESERVED(extPins);

        return extPins.stream().map(ExtPin::getId).collect(Collectors.toList());
    }
}
