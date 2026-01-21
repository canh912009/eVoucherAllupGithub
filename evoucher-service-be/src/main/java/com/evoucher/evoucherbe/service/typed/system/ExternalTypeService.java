package com.evoucher.evoucherbe.service.typed.system;

import com.evoucher.evoucherbe.common.enums.ExternalPinStatus;
import com.evoucher.evoucherbe.common.enums.PeriodType;
import com.evoucher.evoucherbe.entity.ExternalPin;
import com.evoucher.evoucherbe.repository.ExternalPinRepository;
import com.evoucher.evoucherbe.service.request.ExternalPinGiftPopRequest;
import com.evoucher.evoucherbe.service.typed.IntegratedPinService;
import com.evoucher.evoucherbe.service.typed.LockingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("EXTERNAL")
@Slf4j
public class ExternalTypeService extends SystemAbstractService implements IntegratedPinService {
    public ExternalTypeService(SystemBridgeService bridgeService) {
        super(bridgeService);
    }

    @Override
    public List<ExternalPin> getPinsForUsing(Long goodsId, Integer rowNumber, Date expireDate) {
        List<ExternalPin> externalPins;
        log.info(
                "Getting Pin for product EXTERNAL with goodsId: {} and quantity: {}",
                goodsId,
                rowNumber);
        externalPins = bridgeService.getLockingService().getAvailablePins(goodsId, ExternalPinStatus.AVAILABLE, rowNumber, expireDate);

        log.info("Set status RESERVED flag for external PIN: {}",
                externalPins.stream().map(ExternalPin::getId).collect(Collectors.toList()));

        externalPins.forEach(externalPin -> externalPin.setStatus(ExternalPinStatus.RESERVED));
        return bridgeService.getExternalPinRepository().saveAll(externalPins);
    }

    @Override
    public List<Long> callPSToBuyThirdPartyPins(ExternalPinGiftPopRequest payload) {
        return new ArrayList<>();
    }

    @Override
    public void validateGoodExpiredDate(Long id, PeriodType type, Integer periodTerm, String goodExpireDate) {
        log.info("skip validating good expire date by period for external type");
    }
}
