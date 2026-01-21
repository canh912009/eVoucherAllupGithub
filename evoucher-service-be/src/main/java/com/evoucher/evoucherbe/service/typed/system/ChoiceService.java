package com.evoucher.evoucherbe.service.typed.system;

import com.evoucher.evoucherbe.common.enums.PeriodType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("CHOICE")
@Slf4j
public class ChoiceService extends SystemAbstractService {

    public ChoiceService(SystemBridgeService bridgeService) {
        super(bridgeService);
    }

}
