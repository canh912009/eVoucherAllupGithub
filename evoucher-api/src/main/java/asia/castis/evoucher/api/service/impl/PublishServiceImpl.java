package asia.castis.evoucher.api.service.impl;

import asia.castis.evoucher.api.dto.response.*;
import asia.castis.evoucher.api.elastic.repository.PublishEsRepository;
import asia.castis.evoucher.api.elastic.repository.VoucherEsRepository;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.elastic.model.VoucherModel;
import asia.castis.evoucher.api.elastic.model.publish.PublishDetailModel;
import asia.castis.evoucher.api.elastic.model.publish.PublishModel;
import asia.castis.evoucher.api.service.PublishService;
import asia.castis.evoucher.api.utils.ErrorCode;
import asia.castis.evoucher.api.utils.ResponseString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Configuration
@Slf4j
@RequiredArgsConstructor
public class PublishServiceImpl implements PublishService {

    private final PublishEsRepository publishEsRepository;
    private final VoucherEsRepository voucherEsRepository;
    @Override
    public PublishDetails publishByActivationKey(String activationKey) {
        if (Objects.isNull(activationKey) || activationKey.isEmpty()) {
            throw new ApplicationException("Empty activationKey=" + activationKey, ErrorCode.PUBLISH_NOT_FOUND);
        }
        PublishModel publish = publishEsRepository.findByActivationUrlEndingWith(activationKey);
        if (Objects.isNull(publish)) {
            throw new ApplicationException("Can not find publish, activationKey=" + activationKey, ErrorCode.PUBLISH_NOT_FOUND);
        }
        if (Objects.isNull(publish.getPublishDetails()) || publish.getPublishDetails().isEmpty()) {
            log.error("Publish found but has no publish details, publishId={} activationKey={}", publish.getId(), publish.getActivationId());
            throw new ApplicationException("Publish has no voucher to activate, activationKey=" + activationKey, ErrorCode.PUBLISH_NOT_FOUND);
        }
        PublishDetailModel firstPublishDetail = publish.getPublishDetails().stream()
                .filter(detail -> Objects.nonNull(detail.getVoucherId()))
                .min(Comparator.comparing(PublishDetailModel::getId))
                .orElseThrow(() -> new ApplicationException("Can not find publish name and image, activationKey=" + activationKey, ErrorCode.PUBLISH_NOT_FOUND));

        String voucherId = firstPublishDetail.getVoucherId();
        VoucherModel voucherModel = voucherEsRepository.findById(voucherId)
                .orElseThrow(() -> new ApplicationException(ResponseString.VOUCHER_NOT_FOUND, ErrorCode.VOUCHER_NOT_FOUND));

        return PublishDetails.builder().name(voucherModel.getGoods().getName())
                .imagePath(voucherModel.getGoods().getImagePath()).build();
    }
}
