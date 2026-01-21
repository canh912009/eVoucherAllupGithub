package com.evoucher.adminapi.good.service.typed_service.type;

import com.evoucher.adminapi.good.service.typed_service.UsingBalanceService;
import com.evoucher.adminapi.good.service.typed_service.UsingVoucherAbstract;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("bkTypeService")
@Slf4j
@RequiredArgsConstructor
public class BKTypeService implements GoodTypeAbstractService {
    @Getter
    private final UsingBalanceService usingVoucherService;
}
