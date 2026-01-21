package com.evoucher.evoucherbe.service.typed;

import com.evoucher.evoucherbe.common.enums.PeriodType;
import com.evoucher.evoucherbe.common.enums.SystemType;
import com.evoucher.evoucherbe.entity.ExternalPin;
import com.evoucher.evoucherbe.service.request.ExternalPinGiftPopRequest;

import java.util.Date;
import java.util.List;

public interface IntegratedPinService {
    List<SystemType> supported = List.of(SystemType.UR_BOX, SystemType.GIFTPOP, SystemType.WATANE, SystemType.EXTERNAL);
    List<ExternalPin> getPinsForUsing(Long goodsId, Integer rowNumber, Date expireDate);
    List<Long> callPSToBuyThirdPartyPins(ExternalPinGiftPopRequest payload);
}
