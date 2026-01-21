package com.evoucher.evoucherbe.service.typed.system;

import com.evoucher.evoucherbe.common.enums.PeriodType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("INTERNAL")
@Slf4j
public class InternalService extends SystemAbstractService {
    public InternalService(SystemBridgeService bridgeService) {
        super(bridgeService);
    }
}
