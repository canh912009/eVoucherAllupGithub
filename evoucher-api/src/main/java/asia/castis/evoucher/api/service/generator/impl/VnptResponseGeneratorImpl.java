package asia.castis.evoucher.api.service.generator.impl;

import asia.castis.evoucher.api.common.DateUtils;
import asia.castis.evoucher.api.common.enums.EnumResult;
import asia.castis.evoucher.api.common.enums.EnumValidYn;
import asia.castis.evoucher.api.common.enums.VnptCardAction;
import asia.castis.evoucher.api.dto.response.vnpt.*;
import asia.castis.evoucher.api.entity.VnptGoods;
import asia.castis.evoucher.api.entity.VnptProvider;
import asia.castis.evoucher.api.entity.VnptVoucherExchangeHistory;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.repository.VnptGoodsRepository;
import asia.castis.evoucher.api.repository.VnptVoucherExchangeHistoryRepository;
import asia.castis.evoucher.api.service.generator.VnptResponseGenerator;
import asia.castis.evoucher.api.utils.ErrorCode;
import asia.castis.evoucher.api.utils.ResponseString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VnptResponseGeneratorImpl implements VnptResponseGenerator {
    public static final String UNKNOWN = "Unknown";
    public static final int NO_VALUE = 0;
    private final VnptGoodsRepository vnptGoodsRepository;
    private final VnptVoucherExchangeHistoryRepository exchangeHistoryRepository;

    @Override
    public VnptResponse getPurchasedVnptResponse(String ev, Integer goodsId) {
        List<VnptVoucherExchangeHistory> exchangeHistories = exchangeHistoryRepository.findByEvOrderByExchangeDateDesc(ev);
        List<VnptVoucherExchangeHistory> successHistories = exchangeHistories.stream()
                .filter(exchangeHistory -> EnumResult.SUCCESS.equals(exchangeHistory.getResult())
                        || EnumResult.SUCCESS_AFTER_RETRY.equals(exchangeHistory.getResult()))
                .collect(Collectors.toList());
        if (successHistories.isEmpty()) { // Not yet purchased
            throw new ApplicationException("Voucher is supposed to purchased successfully but there is no history", ErrorCode.INVALID_VOUCHER_STATUS);
        }
        if (successHistories.size() > 1) {
            log.warn("Voucher is supposed to purchase successfully only once but there are {} success history, ev={}", successHistories.size(), ev);
            log.warn("Take the latest history id={}", successHistories);
        }
        VnptVoucherExchangeHistory finalHistoryRecord = successHistories.stream().findFirst()
                .orElseThrow(() -> new ApplicationException("Voucher is supposed to purchased successfully but there is no history", ErrorCode.INVALID_VOUCHER_STATUS));

        // Handle topup and cardcode case
        if (VnptCardAction.TOPUP.equals(finalHistoryRecord.getExchangeType())) {
            return VnptResponse.builder()
                    .selectedAction(VnptCardAction.TOPUP)
                    .topupResult(toTopUpResultResponse(finalHistoryRecord))
                    .build();
        } else if (VnptCardAction.CARDCODE.equals(finalHistoryRecord.getExchangeType())) {
            return VnptResponse.builder()
                    .selectedAction(VnptCardAction.CARDCODE)
                    .cardCodeResult(CardCodeResult.builder()
                            .providerCode(Objects.isNull(finalHistoryRecord.getProviderCode()) ? UNKNOWN : finalHistoryRecord.getProviderCode())
                            .faceValue(Objects.isNull(finalHistoryRecord.getFaceValue()) ? NO_VALUE : finalHistoryRecord.getFaceValue())
                            .requestTime(Objects.isNull(finalHistoryRecord.getExchangeDate()) ? DateUtils.getCurrentDateTimeString() : DateUtils.toDateTimeString(finalHistoryRecord.getExchangeDate()))
                            .code(finalHistoryRecord.getCardPin())
                            .serialNo(finalHistoryRecord.getCardSerial())
                            .build())
                    .build();
        } else {
            throw new ApplicationException("Voucher is marked as USED but can not find the history", ErrorCode.INVALID_VOUCHER_STATUS);
        }
    }

    private static TopupResult toTopUpResultResponse(VnptVoucherExchangeHistory finalHistoryRecord) {
        return TopupResult.builder()
                .providerCode(Objects.isNull(finalHistoryRecord.getProviderCode()) ? UNKNOWN : finalHistoryRecord.getProviderCode())
                .targetPhoneNumber(Objects.isNull(finalHistoryRecord.getTargetPhoneNo()) ? UNKNOWN : finalHistoryRecord.getTargetPhoneNo())
                .faceValue(Objects.isNull(finalHistoryRecord.getFaceValue()) ? NO_VALUE : finalHistoryRecord.getFaceValue())
                .requestTime(Objects.isNull(finalHistoryRecord.getExchangeDate()) ? DateUtils.getCurrentDateTimeString() : DateUtils.toDateTimeString(finalHistoryRecord.getExchangeDate()))
                .build();
    }

    @Override
    public VnptResponse getCleanVnptResponse(String ev, Integer goodsId) {
        // Check if the voucher VNPT is in processing
        boolean exists = exchangeHistoryRepository.existsByEvAndResult(ev, EnumResult.PROCESSING);
        if (exists) {
            log.warn("There is a processing transaction for voucher VNPT. ev: {}", ev);
            throw new ApplicationException(ResponseString.VNPT_IS_STILL_IN_PROGRESS, ErrorCode.VNPT_IS_STILL_IN_PROGRESS);
        }

        List<VnptGoods> vnptProducts = vnptGoodsRepository.findByParentGoodsId(goodsId);
        if (vnptProducts.isEmpty()) {
            throw new ApplicationException(String.format("There is no valid goods for this voucher. ev: %s, goodsId: %d", ev, goodsId), ErrorCode.CAN_NOT_FIND_GOODS);
        }
        return VnptResponse.builder()
                .vnptProducts(vnptProducts.stream()
                        .filter(vnptGoods -> EnumValidYn.Y.equals(vnptGoods.getValidYn()))
                        .filter(vnptGoods -> EnumValidYn.Y.equals(vnptGoods.getVnptProvider().getValidYn()))
                        .sorted(Comparator.comparing(vnptGoods -> vnptGoods.getVnptProvider().getProviderNm()))
                        .map(this::toVnptGoodsResponse).collect(Collectors.toList()))
                .build();
    }

    public VnptGoodsResponse toVnptGoodsResponse(VnptGoods vnptGoods) {
        return VnptGoodsResponse.builder()
                .id(vnptGoods.getId())
                .parentGoodsId(vnptGoods.getParentGoodsId())
                .providerCode(vnptGoods.getProviderCode())
                .faceValue(vnptGoods.getFaceValue())
                .validYn(vnptGoods.getValidYn().name())
                .description(vnptGoods.getDescription())
                .vnptProvider(toVnptProviderResponse(vnptGoods.getVnptProvider()))
                .build();
    }

    private VnptProviderResponse toVnptProviderResponse(VnptProvider vnptProvider) {
        return VnptProviderResponse.builder()
                .providerCd(vnptProvider.getProviderCd())
                .providerNm(vnptProvider.getProviderNm())
                .providerType(Objects.isNull(vnptProvider.getProviderType()) ? null : vnptProvider.getProviderType().name())
                .validYn(vnptProvider.getValidYn().name())
                .allowedCardFaces(vnptProvider.getAllowedCardFaces())
                .allowedActions(Objects.isNull(vnptProvider.getAllowedActions()) ? null : vnptProvider.getAllowedActions().name())
                .build();
    }
}
