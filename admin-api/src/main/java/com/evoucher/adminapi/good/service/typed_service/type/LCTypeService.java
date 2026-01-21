package com.evoucher.adminapi.good.service.typed_service.type;

import com.evoucher.adminapi.good.service.typed_service.LimitedCountService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("lcTypeService")
@Slf4j
@RequiredArgsConstructor
public class LCTypeService implements GoodTypeAbstractService {
    @Getter
    private final LimitedCountService usingVoucherService;
}
