package com.evoucher.evoucherbe.service.typed.system;

import com.evoucher.evoucherbe.client.PublishServiceClient;
import com.evoucher.evoucherbe.common.enums.PeriodType;
import com.evoucher.evoucherbe.common.enums.SystemType;
import com.evoucher.evoucherbe.entity.ExternalPin;
import com.evoucher.evoucherbe.repository.ExternalPinRepository;
import com.evoucher.evoucherbe.service.PublishServiceMockingService;
import com.evoucher.evoucherbe.service.request.ExternalPinGiftPopRequest;
import com.evoucher.evoucherbe.service.typed.IntegratedPinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("UR_BOX")
@Slf4j
public class UrBoxService extends SystemAbstractService implements IntegratedPinService {
    public UrBoxService(SystemBridgeService bridgeService) {
        super(bridgeService);
    }

    @Override
    public List<ExternalPin> getPinsForUsing(Long goodsId, Integer rowNumber, Date expireDate) {
        log.info(
                "Getting Pin for product UrBox with goodsId: {} and quantity: {}",
                goodsId,
                rowNumber);
        /**
         * Call api create external pin for GIFTPOP
         * This PIN has been updated to status RESERVED
         */
        List<Long> externalPinIdList = bridgeService.getPublishMockingService().callAPIGet3rdPartyExternalPinId(
                goodsId, rowNumber, SystemType.UR_BOX);

        log.info("Get list ExternalPin with ids: {}", externalPinIdList);
        return bridgeService.getExternalPinRepository().findAllByIdIn(externalPinIdList);
    }

    @Override
    public List<Long> callPSToBuyThirdPartyPins(ExternalPinGiftPopRequest payload) {
        return bridgeService.getPublishServiceClient().buyUrBoxPin(payload);
    }

    @Override
    public void validateGoodExpiredDate(Long id, PeriodType type, Integer periodTerm, String goodExpireDate) {
        log.info("skip validating good expire date for ur box type");
    }


}
