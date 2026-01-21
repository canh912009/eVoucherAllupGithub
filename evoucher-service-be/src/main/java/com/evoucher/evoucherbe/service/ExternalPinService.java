package com.evoucher.evoucherbe.service;

import com.evoucher.evoucherbe.common.enums.ExternalPinStatus;
import com.evoucher.evoucherbe.entity.ExternalPin;
import com.evoucher.evoucherbe.exception.CustomCodeException;
import com.evoucher.evoucherbe.repository.ExternalPinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalPinService {

    private final ExternalPinRepository externalPinRepository;

    public Map<Integer, Integer> getRemainingQuantity(List<Integer> goodsIds) {
        Map<Integer, Integer> result = new HashMap<>();

        if (CollectionUtils.isEmpty(goodsIds)) return result;

        goodsIds.forEach(
                goodsId -> {
                    List<ExternalPin> externalPins = externalPinRepository
                            .findByGoodsIdAndStatus((long) goodsId, ExternalPinStatus.AVAILABLE);
                    result.put(goodsId, externalPins.size());
                });

        return result;
    }

    public List<ExternalPin> getOldestExtPinByNumberAndGoodsIdAndStatus(Long goodsId, Date expireDate, ExternalPinStatus status, Integer number) {
        try {
            log.info("get oldest pin: good-{}, expire date-{}, status-{}, number-{}", goodsId, expireDate, status, number);
            return externalPinRepository.findByStatusAndGoodsIdAndExpireTimeAfterOrderByRegDtAsc(status, goodsId, expireDate, Pageable.ofSize(number)).getContent();
        } catch (Exception e) {
            log.error("error when get oldest available ext pin by number: " + number);
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public List<ExternalPin> getOldestExtPinByNumberAndGoodsIdAndStatusAndIdNotIn(List<Long> exceptId, Long goodsId, Date expireDate, ExternalPinStatus status, Integer number) {
        log.info("get oldest pin: good-{}, expire date-{}, status-{}, number-{} and id not in: {}", goodsId, expireDate, status, number, exceptId);
        if (number == 0) {
            log.error("getting pin number can not be 0");
            return new ArrayList<>();
        }
        try {
            return externalPinRepository.findByIdNotInAndStatusAndGoodsIdAndExpireTimeAfterOrderByRegDtAsc(exceptId, status, goodsId, expireDate, Pageable.ofSize(number)).getContent();
        } catch (Exception e) {
            log.error("error when get oldest available ext pin by number: " + number);
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
