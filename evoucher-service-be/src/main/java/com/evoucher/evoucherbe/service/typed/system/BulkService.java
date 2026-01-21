package com.evoucher.evoucherbe.service.typed.system;

import com.evoucher.evoucherbe.common.enums.PeriodType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("BULK")
@Slf4j
public class BulkService extends SystemAbstractService {

    public BulkService(SystemBridgeService bridgeService) {
        super(bridgeService);
    }
}
