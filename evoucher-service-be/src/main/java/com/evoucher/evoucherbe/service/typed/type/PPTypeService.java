package com.evoucher.evoucherbe.service.typed.type;

import com.evoucher.evoucherbe.service.typed.BalancePayingService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("ppTypeService")
@Slf4j
@RequiredArgsConstructor
public class PPTypeService implements GoodTypeAbstractService {
    @Getter
    private final BalancePayingService payingService;

    @Override
    public boolean isMultipleTimesUsage() {
        return true;
    }
}
