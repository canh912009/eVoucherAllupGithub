package asia.castis.evoucher.api.service.base.impl;

import asia.castis.evoucher.api.common.DateUtils;
import asia.castis.evoucher.api.common.Encryption;
import asia.castis.evoucher.api.common.VoucherUtils;
import asia.castis.evoucher.api.common.enums.SystemType;
import asia.castis.evoucher.api.common.enums.VoucherStatusCode;
import asia.castis.evoucher.api.dto.otpservice.generate.OtpData;
import asia.castis.evoucher.api.dto.otpservice.generate.OtpGenerateTTLRequest;
import asia.castis.evoucher.api.dto.otpservice.generate.OtpResponse;
import asia.castis.evoucher.api.dto.otpservice.restore.VoucherOtpResponse;
import asia.castis.evoucher.api.dto.request.PaymentHistory;
import asia.castis.evoucher.api.dto.response.GoodsResponse;
import asia.castis.evoucher.api.dto.response.PaymentHistoryResponse;
import asia.castis.evoucher.api.dto.response.ResponseData;
import asia.castis.evoucher.api.dto.response.VoucherResponseWrapper;
import asia.castis.evoucher.api.elastic.model.StoreModel;
import asia.castis.evoucher.api.elastic.repository.ElasticPaymentRepository;
import asia.castis.evoucher.api.elastic.repository.ElasticStoreRepository;
import asia.castis.evoucher.api.entity.EVoucher;
import asia.castis.evoucher.api.entity.Publish;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.repository.VoucherRepository;
import asia.castis.evoucher.api.service.BeConnector;
import asia.castis.evoucher.api.service.FeConnector;
import asia.castis.evoucher.api.service.OtpConnector;
import asia.castis.evoucher.api.service.base.BaseVoucherService;
import asia.castis.evoucher.api.service.generator.VoucherResponseGenerator;
import asia.castis.evoucher.api.service.impl.PublishBasicService;
import asia.castis.evoucher.api.utils.ErrorCode;
import asia.castis.evoucher.api.utils.ResponseString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static asia.castis.evoucher.api.service.version1.impl.VoucherServiceImpl.QUANTITY_SUCCESS_CODE;

@Slf4j
@Service
@RequiredArgsConstructor
public class BaseVoucherServiceImpl implements BaseVoucherService {

    public static final int OTP_VOUCHER_SUCCESS_CODE = 200;

    @Value("${otp.expire.time}")
    private long timeVoucherDetailOtp;
    @Value("${otp.expire.timeCancelPayment}")
    private long timeCancelPayment;
    @Value("${otp.expire.voucher.time}")
    private long timeVoucherListOtp;

    public static final int RESEND_OTP_COOLDOWN = 3;

    private final VoucherRepository voucherRepository;
    private final VoucherResponseGenerator voucherResponseGenerator;
    private final OtpConnector otpConnector;
    private final PublishBasicService publishBasicService;
    private final BeConnector beConnector;
    private final FeConnector feConnector;
    private final Encryption encryption;

    private final ElasticPaymentRepository elasticPaymentRepository;
    private final ElasticStoreRepository elasticStoreRepository;

    @Override
    public VoucherResponseWrapper getVoucherDetails(String ev) {
        try {
            log.info("Get voucher details={}", ev);
            EVoucher voucher = voucherRepository.findById(ev)
                    .orElseThrow(() -> new ApplicationException(ResponseString.VOUCHER_NOT_FOUND, ErrorCode.VOUCHER_NOT_FOUND));
            if (!VoucherUtils.isActivated(voucher)) {
                throw new ApplicationException("Voucher is inactive", ErrorCode.VOUCHER_INACTIVE);
            }

            VoucherResponseWrapper responseWrapper = voucherResponseGenerator.getVoucherResponseWrapper(voucher);
            VoucherOtpResponse voucherOtpResponse = otpConnector.generateDefaultOtp(ev);
            if (Objects.nonNull(voucherOtpResponse.getError()) && !voucherOtpResponse.getError().isEmpty()) {
                log.error("Get voucher details error={}", voucherOtpResponse.getError());
                throw new ApplicationException(ResponseString.GENERATE_OTP_FAIL, ErrorCode.GENERATE_OTP_FAIL);
            }
            long currentTime = System.currentTimeMillis();

            responseWrapper.setOtp(voucherOtpResponse.getData().getOtp());
            responseWrapper.setExpireTime(currentTime + timeVoucherDetailOtp * 60 * 1000);

            // Hide sensitive info
            if (!VoucherStatusCode.NORMAL.equals(responseWrapper.getVoucher().getVoucherStatus())) {
                responseWrapper.getVoucher().setExtPinNo(null);
                responseWrapper.getVoucher().setExternalPinPassword(null);
            }

            // Call to backend to get remaining count
            if (SystemType.CHOICE.equals(voucher.getSystem())
                    && Objects.nonNull(responseWrapper.getVoucher().getGoods())
                    && Objects.nonNull(responseWrapper.getVoucher().getGoods().getChoices())
                    && !responseWrapper.getVoucher().getGoods().getChoices().isEmpty()) {
                setRemainingCountChoice(responseWrapper);
            }
            // Get payment history
            if (voucher.getVoucherStatusCode() == VoucherStatusCode.USED || voucher.getVoucherStatusCode() == VoucherStatusCode.PART_USED) {
                List<PaymentHistory> paymentHistoryList = elasticPaymentRepository.getListPaymentByVoucherId(voucher.getEV());
                responseWrapper.setListPaymentHistoryResponse(getPaymentHistoryResponses(paymentHistoryList));
            }

            Publish publish = publishBasicService.findById(voucher.getPublishId());
            responseWrapper.getVoucher().setSenderName(publish.getSenderName());

            return responseWrapper;
        } catch (ApplicationException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Get voucher details exception, msg={}", e.getMessage());
            throw new ApplicationException(ResponseString.VOUCHER_NOT_FOUND, ErrorCode.VOUCHER_NOT_FOUND);
        }
    }

    @Override
    public OtpData getOtpByPhoneNumber(String phoneNumberEncrypt) {
        String phoneNumber;
        try {
            phoneNumber = encryption.decryptData(phoneNumberEncrypt);
        } catch (Exception e) {
            throw new ApplicationException("Exception while decrypting phone number", ErrorCode.VOUCHER_NOT_FOUND);
        }
        try {
            log.info("Get OTP to validate voucher with phoneNumber: " + phoneNumber);
            OtpResponse otpResponse = otpConnector.getOtpByPhoneNo(phoneNumber);

            if (Objects.nonNull(otpResponse.getError()) && !otpResponse.getError().isEmpty()) {
                throw new ApplicationException(ResponseString.OTP_INVALID_OR_EXPIRE, ErrorCode.OTP_INVALID_OR_EXPIRE);
            }

            String dateString = Optional.of(otpResponse)
                    .map(OtpResponse::getData)
                    .map(OtpData::getExpireDtStr).orElse("");

            if (!dateString.isBlank() && otpResponse.getData() != null) {
                otpResponse.getData().setExpireDt(dateString);
            }
            return otpResponse.getData();
        } catch (Exception e) {
            log.error("Error get OTP voucher by phone number: {}", e.getMessage());
            throw new ApplicationException(ResponseString.OTP_INVALID_OR_EXPIRE, ErrorCode.OTP_INVALID_OR_EXPIRE);
        }
    }

    @Override
    public void validateOtp(String encryptedPhoneNumber, String givenOtp) {
        if (Objects.isNull(encryptedPhoneNumber) || encryptedPhoneNumber.isEmpty()) {
            log.error("Null or empty phone number");
            throw new ApplicationException("Null or empty phone number", ErrorCode.VOUCHER_NOT_FOUND);
        }
        String otpByPhoneNo = getOtpByPhoneNumber(encryptedPhoneNumber).getOtp();

        if (!otpByPhoneNo.equals(givenOtp)) {
            log.error("OTP mismatch. Given={}, system={}", givenOtp, otpByPhoneNo);
            throw new ApplicationException(ResponseString.OTP_MISMATCH, ErrorCode.OTP_MISMATCH);
        }
    }

    @Override
    public void createOtp(String encryptedPhoneNo) {
        try {
            boolean otpExist = checkOtpVoucherByPhoneNumberExist(encryptedPhoneNo);

            if (otpExist) {
                log.info("OTP already exits for phone number encrypt: {}", encryptedPhoneNo);
                throw new ApplicationException(
                        ResponseString.OTP_ALREADY_EXIST,
                        ErrorCode.OTP_ALREADY_EXIST);
            }

            String phoneNumber = encryption.decryptData(encryptedPhoneNo);
            VoucherOtpResponse otpResponse = generateOtp(phoneNumber);
            ResponseData<Object> responseData = feConnector.sendOptVoucher(encryptedPhoneNo, otpResponse);
            if (responseData.getCode() != OTP_VOUCHER_SUCCESS_CODE) {
                throw new ApplicationException(responseData.getMessage(), responseData.getCode());
            }
        } catch (ApplicationException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Error create OTP voucher by phone number encrypt: {}", e.getMessage(), e);
            throw new ApplicationException(ResponseString.GENERATE_OTP_FAIL, ErrorCode.GENERATE_OTP_FAIL);
        }
    }

    private void setRemainingCountChoice(VoucherResponseWrapper responseWrapper) throws URISyntaxException {
        List<Long> goodsId = responseWrapper.getVoucher().getGoods().getChoices().stream().map(GoodsResponse::getId).collect(Collectors.toList());
        ResponseData<Map<Long, Integer>> goodsQuantityResponse = callBeApiGetRemainingCount(goodsId);
        if (QUANTITY_SUCCESS_CODE == goodsQuantityResponse.getCode()) {
            Map<Long, Integer> remainingCount = goodsQuantityResponse.getData();
            responseWrapper.getVoucher().getGoods().getChoices().forEach(o -> o.setRemainingCount(remainingCount.getOrDefault(o.getId(), 0)));
        }
    }

    private ResponseData<Map<Long, Integer>> callBeApiGetRemainingCount(List<Long> goodsId) throws URISyntaxException {
        ResponseData<Map<Long, Integer>> goodsQuantityResponse = beConnector.getRemainingCount(goodsId);
        log.info("Goods quantity response={}", goodsQuantityResponse);
        return goodsQuantityResponse;
    }

    private List<PaymentHistoryResponse> getPaymentHistoryResponses(List<PaymentHistory> paymentHistoryList) {
        List<PaymentHistoryResponse> paymentHistoryResponseList = new ArrayList<>();
        for (PaymentHistory item : paymentHistoryList) {
            try {
                PaymentHistoryResponse paymentHistoryResponse = new PaymentHistoryResponse();
                Date transactionDate = DateUtils.toDateTime(item.getTransactionDate());
                if (Objects.nonNull(transactionDate)) {
                    paymentHistoryResponse.setTimeExpireCancel(transactionDate.getTime() + cancelDurationInMilliseconds());
                }
                StoreModel storeModel = elasticStoreRepository.searchById(item.getStoreId());
                paymentHistoryResponse.setStoreModel(storeModel);
                item.setVoucherId("");
                paymentHistoryResponse.setPaymentHistory(item);
                paymentHistoryResponseList.add(paymentHistoryResponse);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return paymentHistoryResponseList;
    }

    private boolean checkOtpVoucherByPhoneNumberExist(String phoneNumberEncrypt) {
        try {
            var otpVoucher = getOtpByPhoneNumber(phoneNumberEncrypt);
            if (Objects.isNull(otpVoucher) || Objects.isNull(otpVoucher.getRegDt())) {
                return false;
            }
            log.info("otp voucher is not null, reg date is not null");

            if (!otpIsCoolingDown(otpVoucher.getRegDt())) {
                return false;
            }
            log.info("otp was created less than 3 minutes before");

            return Objects.nonNull(otpVoucher.getOtp());
        } catch (Exception e) {
            log.warn("{} -> Need to create new OTP", e.getMessage());
            return false;
        }
    }


    private boolean otpIsCoolingDown(Long regDtInMilliseconds) {
        Date regDate = Date.from(Instant.ofEpochMilli(regDtInMilliseconds));
        DateTime regDt = new DateTime(regDate);
        return regDt.plusMinutes(RESEND_OTP_COOLDOWN).isAfter(DateTime.now());
    }
    private VoucherOtpResponse generateOtp(String phoneNumber) {

        Long numberOfSecondExpire = timeVoucherListOtp * 60;
        Integer optCharactersLength = 6;
        OtpGenerateTTLRequest requestEntity = OtpGenerateTTLRequest
                .builder()
                .key(phoneNumber)
                .ttl(numberOfSecondExpire)
                .length(optCharactersLength)
                .build();
        return otpConnector.generateOtpWithTTL(requestEntity);
    }

    private long cancelDurationInMilliseconds() {
        return timeCancelPayment * 24 * 60 * 60 * 1000;
    }
}
