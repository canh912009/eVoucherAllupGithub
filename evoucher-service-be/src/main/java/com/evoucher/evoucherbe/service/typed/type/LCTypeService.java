package com.evoucher.evoucherbe.service.typed.type;

import com.evoucher.evoucherbe.service.typed.LimitedCountService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("lcTypeService")
@Slf4j
@RequiredArgsConstructor
public class LCTypeService implements GoodTypeAbstractService {
    @Getter
    private final LimitedCountService payingService;

    @Override
    public boolean isMultipleTimesUsage() {
        return true;
    }
}
