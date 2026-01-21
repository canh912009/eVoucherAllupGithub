package com.evoucher.adminapi.good.service.typed_service.type;

import com.evoucher.adminapi.good.service.typed_service.UsingBalanceService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("chTypeService")
@Slf4j
@RequiredArgsConstructor
public class CHTypeService implements GoodTypeAbstractService {
    @Getter
    private final UsingBalanceService usingVoucherService;
    public boolean isMultipleTimesUsage() {
        return true;
    }
}
