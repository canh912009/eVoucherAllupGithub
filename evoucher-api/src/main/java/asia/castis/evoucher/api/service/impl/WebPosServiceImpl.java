package asia.castis.evoucher.api.service.impl;

import asia.castis.evoucher.api.common.Constant;
import asia.castis.evoucher.api.common.DateUtils;
import asia.castis.evoucher.api.common.Encryption;
import asia.castis.evoucher.api.common.enums.*;
import asia.castis.evoucher.api.dto.otpservice.use.VoucherUuidResponse;
import asia.castis.evoucher.api.dto.request.CancelPaymentRequest;
import asia.castis.evoucher.api.dto.request.ExchangeRequest;
import asia.castis.evoucher.api.dto.request.PaymentHistory;
import asia.castis.evoucher.api.dto.response.ExchangeResponse;
import asia.castis.evoucher.api.dto.response.PaymentHistoryResponse;
import asia.castis.evoucher.api.dto.response.ResponseData;
import asia.castis.evoucher.api.dto.response.VoucherResponse;
import asia.castis.evoucher.api.elastic.enums.EnumExchangeType;
import asia.castis.evoucher.api.elastic.model.publish.RabbitPaymentHistory;
import asia.castis.evoucher.api.elastic.repository.ElasticPaymentRepository;
import asia.castis.evoucher.api.elastic.repository.ElasticPublishRepository;
import asia.castis.evoucher.api.elastic.repository.ElasticVoucherRepository;
import asia.castis.evoucher.api.entity.EVoucher;
import asia.castis.evoucher.api.entity.Goods;
import asia.castis.evoucher.api.entity.Store;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.mapper.ElasticVoucherMapper;
import asia.castis.evoucher.api.publishrequest.sender.RabbitMQSender;
import asia.castis.evoucher.api.repository.GoodsRepository;
import asia.castis.evoucher.api.repository.StoreRepository;
import asia.castis.evoucher.api.repository.VoucherRepository;
import asia.castis.evoucher.api.service.BeConnector;
import asia.castis.evoucher.api.service.OtpConnector;
import asia.castis.evoucher.api.service.WebPosService;
import asia.castis.evoucher.api.service.generator.PaymentHistoryGenerator;
import asia.castis.evoucher.api.service.generator.VoucherResponseGenerator;
import asia.castis.evoucher.api.utils.ErrorCode;
import asia.castis.evoucher.api.utils.ResponseString;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Configuration
@Slf4j
@RequiredArgsConstructor
public class WebPosServiceImpl implements WebPosService {

    private final BeConnector beConnector;
    private final StoreRepository storeRepository;
    private final GoodsRepository goodsRepository;
    @Value("${otp.expire.timeCancelPayment}")
    private long timeCancelPayment;

    private final VoucherRepository voucherRepository;
    private final ElasticVoucherRepository elasticVoucherRepository;
    private final ElasticPaymentRepository elasticPaymentRepository;
    private final ElasticPublishRepository elasticPublishRepository;
    private final RabbitMQSender rabbitMQSender;
    private final Encryption encryption;
    private final OtpConnector otpConnector;
    private final VoucherResponseGenerator voucherResponseGenerator;
    private final PaymentHistoryGenerator paymentHistoryGenerator;

    private static final ElasticVoucherMapper voucherMapper = ElasticVoucherMapper.INSTANCE;
    private static final Gson gson = new Gson();

    /**
     * 3.2.4: EVoucher usage (for web POS or PP ev)
     *
     * @param otp otp
     * @return VoucherResponse
     */
    @Override
    public VoucherResponse voucherDetailByOtp(String otp) {
        try {
            log.info("Get voucher details by OTP: {}", otp);
            VoucherUuidResponse otpResponse = otpConnector.getUuidByOtp(otp);
            String voucherId = otpResponse.getData().getUuid();
            if (Objects.isNull(voucherId) || voucherId.isEmpty()) {
                throw new ApplicationException(ResponseString.VOUCHER_NOT_FOUND, ErrorCode.VOUCHER_NOT_FOUND);
            }
            return voucherResponseGenerator.getVoucherResponse(voucherId);
        } catch (ApplicationException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(ResponseString.VOUCHER_NOT_FOUND, ErrorCode.VOUCHER_NOT_FOUND);
        }
    }

    /**
     * @param paymentHistoryId long
     * @return PaymentHistoryResponse
     */
    @Override
    public PaymentHistoryResponse paymentDetail(String paymentHistoryId) {
        return null;
    }

    @Override
    public ExchangeResponse cancelPayment(CancelPaymentRequest cancelPaymentRequest) {
        try {
            log.info("Cancel payment: {}", gson.toJson(cancelPaymentRequest));
            PaymentHistory paymentHistory = elasticPaymentRepository.searchById(cancelPaymentRequest.getPaymentHistoryId());
            //Get payment info by id
            if (Objects.isNull(paymentHistory)) {
                log.error("Can not find payment history, id={}", cancelPaymentRequest.getPaymentHistoryId());
                throw new ApplicationException(ResponseString.PAYMENT_HISTORY_NOT_FOUND, ErrorCode.PAYMENT_HISTORY_NOT_FOUND);
            }
            log.info("Found payment history: {}", paymentHistory);
            //Check storeId is the same or not?
            validateCancelPaymentRequest(cancelPaymentRequest, paymentHistory);
            //Get voucher by voucher Id
            EVoucher voucher = voucherRepository.findById(paymentHistory.getVoucherId())
                    .orElseThrow(() -> new ApplicationException(ResponseString.VOUCHER_NOT_FOUND, ErrorCode.VOUCHER_NOT_FOUND));
            log.info("Found voucher ev={}", voucher.getEV());
            //Check voucher expire
            validateCancelVoucher(voucher);

            PaymentHistory cancelHistory = getCancelPaymentHistory(cancelPaymentRequest, paymentHistory);
            elasticPaymentRepository.createPaymentVoucher(cancelHistory);

            rabbitMQSender.sendPaymentHistory(createRabbitPaymentHistory(cancelHistory));
            sendCancelRequestToBe(cancelHistory);

            ExchangeResponse exchangeResponse = getExchangeResponse(cancelHistory);
            log.info("END cancel payment: {}", gson.toJson(cancelPaymentRequest));
            return exchangeResponse;
        } catch (ApplicationException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(ResponseString.UNKNOWN_ERROR, ErrorCode.UNKNOWN_ERROR);
        }
    }

    private static PaymentHistory getCancelPaymentHistory(CancelPaymentRequest cancelPaymentRequest, PaymentHistory paymentHistory) {
        PaymentHistory cancelHistory = new PaymentHistory();
        BeanUtils.copyProperties(paymentHistory, cancelHistory);
        //Update payment history
        cancelHistory.setId(UUID.randomUUID().toString());
        cancelHistory.setExchangeType(EnumExchangeType.CANCEL);
        cancelHistory.setTransactionDate(DateUtils.getCurrentDateTimeString());
        cancelHistory.setPosCd(cancelPaymentRequest.getPosCd());
        cancelHistory.setApprovementNo(cancelPaymentRequest.getApprovementNo());
        cancelHistory.setPosVerType(cancelPaymentRequest.getPosVerType());
        log.info("cancel payment: {}", cancelHistory);
        return cancelHistory;
    }

    private static void validateCancelVoucher(EVoucher voucher) {
        if (voucher.getExpirationDate().before(new Date())) {
            log.error("Voucher is already expired: {}", ResponseString.VOUCHER_IS_EXPIRE);
            throw new ApplicationException(ResponseString.VOUCHER_IS_EXPIRE, ErrorCode.VOUCHER_IS_EXPIRE);
        }
        //Check voucher status
        if (voucher.getVoucherTypeCode() == VoucherTypeCode.PP
                && !(voucher.getVoucherStatusCode() == VoucherStatusCode.PART_USED || voucher.getVoucherStatusCode() == VoucherStatusCode.USED)) {
            log.error("Voucher not has not been used");
            throw new ApplicationException(ResponseString.CANCEL_VOUCHER_NOT_USE, ErrorCode.CANCEL_VOUCHER_NOT_USE);
        }
    }

    private void validateCancelPaymentRequest(CancelPaymentRequest cancelPaymentRequest, PaymentHistory paymentHistory) {
        if (!cancelPaymentRequest.getStoreId().equals(paymentHistory.getStoreId())) {
            log.error("Must cancel request in the same store: cancel_payment={}, cancel_store={}, orig_store={}",
                    cancelPaymentRequest.getStoreId(), cancelPaymentRequest.getStoreId(), paymentHistory.getStoreId());
            throw new ApplicationException(ResponseString.CANCEL_STORE_NOT_SAME, ErrorCode.CANCEL_STORE_NOT_SAME);
        }
        //Check payment cancel expire
        Date transactionDate = DateUtils.toDateTime(paymentHistory.getTransactionDate());
        if (Objects.isNull(transactionDate)) {
            log.error("Can not get payment time");
            throw new ApplicationException(ResponseString.CANCEL_PAYMENT_EXPIRE, ErrorCode.CANCEL_PAYMENT_EXPIRE);
        }
        long transactionMillisecond = transactionDate.getTime();
        long currentMillisecond = new Date().getTime();
        if (currentMillisecond - transactionMillisecond > getCancelPaymentValidDuration()) {
            log.error("Cancel payment request is too late > {} days", getCancelPaymentValidDuration());
            throw new ApplicationException(ResponseString.CANCEL_PAYMENT_EXPIRE, ErrorCode.CANCEL_PAYMENT_EXPIRE);
        }
        //Check payment history status
        //if the status is used can not cancel the payment
        if (paymentHistory.getExchangeType() != EnumExchangeType.USE) {
            log.error("No use history found" + ResponseString.CANCEL_VOUCHER_NOT_USE);
            throw new ApplicationException(ResponseString.CANCEL_VOUCHER_PAYMENT_NOT_USE, ErrorCode.CANCEL_VOUCHER_PAYMENT_NOT_USE);
        }
    }

    private long getCancelPaymentValidDuration() {
        return timeCancelPayment * 24 * 60 * 60 * 1000;
    }

    /**
     * 3.2.4: EVoucher usage (for web POS or PP ev)
     *
     * @param exchangeRequest ExchangeRequest
     * @return VoucherResponse
     */
    @Override
    public ExchangeResponse exchangeVoucher(ExchangeRequest exchangeRequest) {
        try {
            log.info("START exchange voucher: {}", gson.toJson(exchangeRequest));
            String voucherId = getVoucherIdFromOTP(exchangeRequest);
            EVoucher voucher = voucherRepository.findById(voucherId)
                    .orElseThrow(() -> new ApplicationException(ResponseString.VOUCHER_NOT_FOUND, ErrorCode.VOUCHER_NOT_FOUND));

            validateVoucher(voucher, exchangeRequest);

            Goods goods = goodsRepository.findById((int) exchangeRequest.getGoodsId())
                    .orElseThrow(() -> new ApplicationException(ResponseString.CAN_NOT_FIND_GOODS, ErrorCode.CAN_NOT_FIND_GOODS));
            //Check storeId is the same or not?
            Store store = storeRepository.findById(exchangeRequest.getStoreId())
                    .orElseThrow(() -> new ApplicationException(ResponseString.STORE_NOT_FOUND, ErrorCode.STORE_NOT_FOUND));

            if (store.getValidYn() == EnumValidYn.N) {
                throw new ApplicationException(ResponseString.INVALID_STORE, ErrorCode.INVALID_STORE);
            }

            if (goods.getStoreQueryType().equals(StoreQueryType.EXCLUDE)
                    && Objects.nonNull(goods.getExceptStoreIds())
                    && goods.getExceptStoreIds().contains(store.getId())) {
                throw new ApplicationException(ResponseString.INVALID_STORE, ErrorCode.INVALID_STORE);
            }

            if (Objects.isNull(store.getBrandId()) || !store.getBrandId().equals(goods.getBrandId())) {
                throw new ApplicationException(ResponseString.INVALID_STORE, ErrorCode.INVALID_STORE);
            }

            PaymentHistory paymentHistory = paymentHistoryGenerator.generateExchangePaymentHistory(voucher, goods, exchangeRequest);

            // Save payment history model to ES
            elasticPaymentRepository.createPaymentVoucher(paymentHistory);

            sendExchangeRequestToBe(paymentHistory);
            log.info("END exchange voucher: {}", gson.toJson(exchangeRequest));
            return getExchangeResponse(paymentHistory);
        } catch (ApplicationException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(ResponseString.UNKNOWN_ERROR, ErrorCode.UNKNOWN_ERROR);
        }
    }

    private void sendCancelRequestToBe(PaymentHistory cancelRequest) {
        log.info("Send request cancel to BE: {}  ", cancelRequest);
        ResponseData<?> responseData = beConnector.cancelExchange(cancelRequest);

        if (responseData.getCode() != Constant.BE_SUCCESS_CODE) {
            throw new ApplicationException(responseData.getMessage(), responseData.getCode());
        }
    }

    private void sendExchangeRequestToBe(PaymentHistory exchangeRequest) {
        log.info("Send request exchange to BE: {}  ", exchangeRequest);
        ResponseData<?> responseData = beConnector.purchase(exchangeRequest);

        if (responseData.getCode() != Constant.BE_SUCCESS_CODE) {
            throw new ApplicationException(responseData.getMessage(), responseData.getCode());
        }
    }

    private static ExchangeResponse getExchangeResponse(PaymentHistory paymentHistory) {
        ExchangeResponse exchangeResponse = new ExchangeResponse();
        exchangeResponse.setPaymentHistoryId(paymentHistory.getId());
        exchangeResponse.setApprovementNo(paymentHistory.getApprovementNo());
        exchangeResponse.setSuccess(true);
        return exchangeResponse;
    }

    private void validateVoucher(EVoucher voucher, ExchangeRequest exchangeRequest) {
        log.info("Validating voucher ev={}", voucher.getEV());
        if (voucher.getVoucherStatusCode() == VoucherStatusCode.DISABLED) {
            throw new ApplicationException(ResponseString.VOUCHER_IS_DISABLE, ErrorCode.VOUCHER_IS_DISABLE);
        }

        if (voucher.getVoucherStatusCode() == VoucherStatusCode.USED) {
            throw new ApplicationException(ResponseString.VOUCHER_USED, ErrorCode.VOUCHER_USED);
        }

        if (voucher.getVoucherStatusCode() == VoucherStatusCode.EXPIRE) {
            throw new ApplicationException(ResponseString.VOUCHER_IS_EXPIRE, ErrorCode.VOUCHER_IS_EXPIRE);
        }
        if (voucher.getExpirationDate().before(new Date())) {
            throw new ApplicationException(ResponseString.VOUCHER_IS_EXPIRE, ErrorCode.VOUCHER_IS_EXPIRE);
        }
        if (voucher.getTransferStatusCode() == TransferStatusCode.RECPT_WAIT) {
            throw new ApplicationException(ResponseString.VOUCHER_TRANSFER_PROCESSING, ErrorCode.VOUCHER_TRANSFER_PROCESSING);
        }
        if (voucher.getVoucherTypeCode() == VoucherTypeCode.PP && voucher.getBalance() < exchangeRequest.getPaymentAmount()) {
            throw new ApplicationException(ResponseString.VOUCHER_LIMIT_AMOUNT, ErrorCode.VOUCHER_LIMIT_AMOUNT);
        }
        if (voucher.getVoucherTypeCode() == VoucherTypeCode.LC
                && (Objects.isNull(voucher.getUsageRemainingCount()) || voucher.getUsageRemainingCount() <= 0)) {
            throw new ApplicationException(ResponseString.VOUCHER_LIMIT_AMOUNT, ErrorCode.VOUCHER_LIMIT_AMOUNT);
        }
    }

    /**
     * 1. Get uuid from otp to check if otp is valid
     * 2. use otp to get voucher uuid (ev)
     *
     * @param exchangeRequest
     * @return
     */
    private String getVoucherIdFromOTP(ExchangeRequest exchangeRequest) {
        // Check otp valid
        getUuidFromOtp(exchangeRequest.getOtp());
        log.info("OTP is valid: {}", exchangeRequest.getOtp());
        // Get ev
        String voucherId = useOtp(exchangeRequest.getOtp());
        log.info("Voucher used: ev={}, otp={}", voucherId, exchangeRequest.getOtp());
        if (Objects.isNull(voucherId) || voucherId.isEmpty()) {
            log.info("voucherId is null or empty");
            throw new ApplicationException(ResponseString.VOUCHER_NOT_FOUND, ErrorCode.VOUCHER_NOT_FOUND);
        }
        return voucherId;
    }

    @Override
    public List<PaymentHistoryResponse> getListPaymentHistory(String request) {
        return null;
    }

    private RabbitPaymentHistory createRabbitPaymentHistory(PaymentHistory paymentHistory) {

        RabbitPaymentHistory history = new RabbitPaymentHistory(paymentHistory.getId(), paymentHistory.getExchangeType(),
                paymentHistory.getTransactionDate(), paymentHistory.getStoreId(),
                paymentHistory.getVoucherId(), paymentHistory.getVoucherTypeCode(), paymentHistory.getGoodsId(),
                paymentHistory.getGoodsName(), paymentHistory.getListPrice(), paymentHistory.getDiscountRate(),
                paymentHistory.getDiscountAmount(), paymentHistory.getExchangeAmount(), paymentHistory.getUserMobileNumber(),
                paymentHistory.getStaffMobileNumber(), paymentHistory.getVoucherStatus(), paymentHistory.getBalance());
        log.info("send to rabbit: {}", history);
        return history;
    }


    private String getUuidFromOtp(String otp) throws ApplicationException {
        try {
            VoucherUuidResponse getResponse = otpConnector.getOtp(otp);
            if ((Objects.nonNull(getResponse.getError()) && !getResponse.getError().isEmpty())
                    || Objects.isNull(getResponse.getData())) {
                throw new ApplicationException(ResponseString.OTP_INVALID_OR_EXPIRE, ErrorCode.OTP_INVALID_OR_EXPIRE);
            }
            return getResponse.getData().getUuid();
        } catch (ApplicationException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ResponseString.UNKNOWN_ERROR, ErrorCode.UNKNOWN_ERROR);
        }
    }

    private String useOtp(String otp) throws ApplicationException {
        try {
            VoucherUuidResponse getResponse = otpConnector.useOtp(otp);
            if ((Objects.nonNull(getResponse.getError()) && !getResponse.getError().isEmpty())
                    || Objects.isNull(getResponse.getData())) {
                throw new ApplicationException(ResponseString.OTP_INVALID_OR_EXPIRE, ErrorCode.OTP_INVALID_OR_EXPIRE);
            }
            return getResponse.getData().getUuid();
        } catch (ApplicationException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ResponseString.UNKNOWN_ERROR, ErrorCode.UNKNOWN_ERROR);
        }
    }
}
