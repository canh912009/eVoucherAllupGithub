package com.evoucher.evoucherbe.service.typed.type;

import com.evoucher.evoucherbe.entity.EVoucher;
import com.evoucher.evoucherbe.service.typed.PayingService;

public interface GoodTypeAbstractService {
    PayingService getPayingService();
    boolean isMultipleTimesUsage();
}
