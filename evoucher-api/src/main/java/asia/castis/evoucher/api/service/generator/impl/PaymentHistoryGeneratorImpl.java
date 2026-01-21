package asia.castis.evoucher.api.service.generator.impl;

import asia.castis.evoucher.api.common.DateUtils;
import asia.castis.evoucher.api.common.Encryption;
import asia.castis.evoucher.api.common.StringUtils;
import asia.castis.evoucher.api.common.enums.SystemType;
import asia.castis.evoucher.api.common.enums.VnptCardAction;
import asia.castis.evoucher.api.common.enums.VoucherTypeCode;
import asia.castis.evoucher.api.dto.request.ExchangeRequest;
import asia.castis.evoucher.api.dto.request.PaymentHistory;
import asia.castis.evoucher.api.dto.request.vnpt.VnptPurchaseRequest;
import asia.castis.evoucher.api.elastic.enums.EnumExchangeType;
import asia.castis.evoucher.api.elastic.enums.PosType;
import asia.castis.evoucher.api.entity.EVoucher;
import asia.castis.evoucher.api.entity.Goods;
import asia.castis.evoucher.api.service.generator.PaymentHistoryGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentHistoryGeneratorImpl implements PaymentHistoryGenerator {
    private static final Double DEFAULT_DISCOUNT_AMOUNT = (double) 0;
    private static final Double DEFAULT_DISCOUNT_RATE = (double) 0;
    public static final int DEFAULT_LIMITED_COUNT_EXCHANGE_VALUE = 1;
    public static final double DEFAULT_BALANCE = 0.0;
    private final Encryption encryption;
    @Override
    public PaymentHistory getVnptPaymentHistory(EVoucher sourceVoucher, Goods goods, VnptPurchaseRequest vnptPurchaseRequest) {
        // All default values
        PaymentHistory.PaymentHistoryBuilder historyBuilder = PaymentHistory.builder()
                .id(UUID.randomUUID().toString())
                .exchangeType(EnumExchangeType.USE)
                .transactionDate(DateUtils.getCurrentDateTimeString())
                .discountAmount(DEFAULT_DISCOUNT_AMOUNT)
                .userMobileNumber(null)
                .posType(PosType.WEBPOS)
                .voucherStatus(null)
                .balance(DEFAULT_BALANCE)
                .storeId(null)
                .staffMobileNumber(null)
                .approvementNo(null)
                .posCd(null)
                .optInputType(null)
                .posVerType(null);

        // Voucher related values
        historyBuilder
                .voucherId(sourceVoucher.getEV())
                .voucherTypeCode(sourceVoucher.getVoucherTypeCode())
                .initAmount(sourceVoucher.getInitAmount())
                .discountRate(sourceVoucher.getDiscountRate())
                .voucherTransferStatus(sourceVoucher.getTransferStatusCode());

        // Goods related values
        historyBuilder
                .goodsId(goods.getId())
                .goodsName(goods.getGoodsName())
                .listPrice(goods.getListPrice())
                .exchangeAmount(goods.getSellPrice());

        // VNPT Epay
        historyBuilder
                .vnptExchangeType(VnptCardAction.valueOf(vnptPurchaseRequest.getAction()))
                .vnptProviderCode(vnptPurchaseRequest.getProviderCode());
        if (VnptCardAction.TOPUP.equals(VnptCardAction.valueOf(vnptPurchaseRequest.getAction()))) {
            historyBuilder
                    .vnptReceiverPhoneNo(encryption.encryptData(vnptPurchaseRequest.getReceiverPhoneNo()));
        }

        return historyBuilder.build();
    }

    @Override
    public PaymentHistory generateExchangePaymentHistory(EVoucher sourceVoucher, Goods goods, ExchangeRequest exchangeRequest) {
        // All default values
        PaymentHistory.PaymentHistoryBuilder paymentHistoryBuilder = PaymentHistory.builder()
                .id(UUID.randomUUID().toString())
                .exchangeType(EnumExchangeType.USE)
                .transactionDate(DateUtils.getCurrentDateTimeString())
                .discountAmount(DEFAULT_DISCOUNT_AMOUNT)
                .discountRate(DEFAULT_DISCOUNT_RATE)
                .exchangeAmount(getExchangeAmount(goods, exchangeRequest))
                .voucherStatus(null)
                .balance(DEFAULT_BALANCE)
                .approvementNo(getApprovalNo(exchangeRequest));

        // Exchange request related
        paymentHistoryBuilder.storeId(exchangeRequest.getStoreId())
                .goodsId(exchangeRequest.getGoodsId())
                .goodsName(exchangeRequest.getGoodsName())
                .listPrice(exchangeRequest.getListPrice())
                .staffMobileNumber(exchangeRequest.getStaffMobileNum())
                .optInputType(exchangeRequest.getOtpInputType())
                .posType(exchangeRequest.getPosType())
                .posCd(exchangeRequest.getPosCd())
                .posVerType(exchangeRequest.getPosVerType());

        // Voucher related
        paymentHistoryBuilder.voucherId(sourceVoucher.getEV())
                .voucherTypeCode(sourceVoucher.getVoucherTypeCode())
                .userMobileNumber(sourceVoucher.getUserMobileNumber())
                .voucherTransferStatus(sourceVoucher.getTransferStatusCode())
                .initAmount(sourceVoucher.getInitAmount());

        PaymentHistory paymentHistory = paymentHistoryBuilder.build();
        log.info("PaymentHistory: {}", paymentHistory);
        return paymentHistory;
    }

    private static String getApprovalNo(ExchangeRequest exchangeRequest) {
        String approvementNo = System.currentTimeMillis() + exchangeRequest.getStoreId();
        if (approvementNo.length() < 20) {
            approvementNo = approvementNo + StringUtils.generateRandom(20);
        }
        if (approvementNo.length() > 20) {
            approvementNo = approvementNo.substring(0, 20);
        }
        return approvementNo;
    }

    private static double getExchangeAmount(Goods goods, ExchangeRequest exchangeRequest) {
        if (goods.getSystem() == SystemType.INTERNAL) {
            if (goods.getGoodsType() == VoucherTypeCode.SI) {
                return goods.getSellPrice();
            } else if (goods.getGoodsType() == VoucherTypeCode.PP) {
                return exchangeRequest.getPaymentAmount();
            } else if (goods.getGoodsType() == VoucherTypeCode.LC) {
                return DEFAULT_LIMITED_COUNT_EXCHANGE_VALUE;
            }
        }
        return goods.getSellPrice();
    }
}
