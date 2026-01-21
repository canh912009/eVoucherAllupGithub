package com.castis.publishservice.service.external_pin;

import com.castis.publishservice.dto.GoodsDTO;
import com.castis.publishservice.entity.ExtPin;

import java.util.Date;
import java.util.List;

public interface IntegratedPinService {
    void reservePin(Integer userSize, GoodsDTO goods, Date bookingDate);

    List<ExtPin> getAvailablePin(int totalSize, GoodsDTO goods, Date bookingDate);

    List<ExtPin> getReservedPin(int totalSize, GoodsDTO goods, Date bookingDate);

//    void cancelReservedPins();
}
