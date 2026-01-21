package asia.castis.evoucher.api.service.version1.impl;

import asia.castis.evoucher.api.common.Constant;
import asia.castis.evoucher.api.common.DateUtils;
import asia.castis.evoucher.api.common.Encryption;
import asia.castis.evoucher.api.common.VoucherUtils;
import asia.castis.evoucher.api.common.enums.SystemType;
import asia.castis.evoucher.api.common.enums.TransferStatusCode;
import asia.castis.evoucher.api.common.enums.VoucherStatusCode;
import asia.castis.evoucher.api.common.enums.VoucherTypeCode;
import asia.castis.evoucher.api.dto.otpservice.generate.OtpData;
import asia.castis.evoucher.api.dto.otpservice.generate.OtpResponse;
import asia.castis.evoucher.api.dto.otpservice.use.VoucherUuidResponse;
import asia.castis.evoucher.api.dto.request.ReceiptRequest;
import asia.castis.evoucher.api.dto.request.TransferVoucherRequest;
import asia.castis.evoucher.api.dto.request.UserVoucherListRequest;
import asia.castis.evoucher.api.dto.request.activate.ActivateRequestV1;
import asia.castis.evoucher.api.dto.response.PageResponse;
import asia.castis.evoucher.api.dto.response.ResponseData;
import asia.castis.evoucher.api.dto.response.VoucherResponse;
import asia.castis.evoucher.api.dto.response.VoucherResponseWrapper;
import asia.castis.evoucher.api.elastic.enums.EnumVoucherTransferStatus;
import asia.castis.evoucher.api.elastic.model.VoucherModel;
import asia.castis.evoucher.api.elastic.repository.ElasticVoucherRepository;
import asia.castis.evoucher.api.elastic.repository.VoucherEsRepository;
import asia.castis.evoucher.api.entity.EVoucher;
import asia.castis.evoucher.api.entity.VoucherTransferHistory;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.exception.InvalidException;
import asia.castis.evoucher.api.publishrequest.EndUserBERequest;
import asia.castis.evoucher.api.publishrequest.PublishReceiptVoucher;
import asia.castis.evoucher.api.publishrequest.PublishTransferVoucher;
import asia.castis.evoucher.api.repository.VoucherRepository;
import asia.castis.evoucher.api.repository.VoucherTransferHistoryRepository;
import asia.castis.evoucher.api.service.BeConnector;
import asia.castis.evoucher.api.service.FeConnector;
import asia.castis.evoucher.api.service.OtpConnector;
import asia.castis.evoucher.api.service.PsConnector;
import asia.castis.evoucher.api.service.base.BaseVoucherService;
import asia.castis.evoucher.api.service.generator.VoucherResponseGenerator;
import asia.castis.evoucher.api.service.impl.PublishBasicService;
import asia.castis.evoucher.api.service.version1.VoucherService;
import asia.castis.evoucher.api.utils.ErrorCode;
import asia.castis.evoucher.api.utils.ResponseString;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Configuration
@Slf4j
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    public static final int QUANTITY_SUCCESS_CODE = 0;

    private final ElasticVoucherRepository _elasticVoucherRepository;
    private final VoucherEsRepository _voucherEsRepository;
    private final Encryption encryption;
    private final FeConnector feConnector;
    private final OtpConnector otpConnector;
    private final BeConnector beConnector;
    private final VoucherRepository voucherRepository;
    private final VoucherResponseGenerator voucherResponseGenerator;
    private final VoucherTransferHistoryRepository transferHistoryRepository;
    private final PublishBasicService publishBasicService;
    private final PsConnector psConnector;
    private final BaseVoucherService baseVoucherService;

    private static final Gson gson = new Gson();

    /**
     * 3.2.6 receiving hand overed EVoucher
     *
     * @param receiptRequest ReceiptRequest
     * @return ReceiptResponse
     */
    @Override
    public VoucherResponse confirmReceipt(ReceiptRequest receiptRequest) {
        try {
            log.info("[Confirm voucher] Start req={}", receiptRequest);
            //7. Otp validity check request
            VoucherUuidResponse useResponse = otpConnector.useOtp(receiptRequest.getOtp());
            if (Objects.nonNull(useResponse.getError()) && !useResponse.getError().isEmpty()) {
                throw new ApplicationException(ResponseString.OTP_INVALID_OR_EXPIRE, ErrorCode.OTP_INVALID_OR_EXPIRE);
            }
            String voucherId = useResponse.getData().getUuid();
            if (Objects.isNull(voucherId) || voucherId.isEmpty()) {
                log.info("[Confirm voucher] Null or empty data returned from OTP service otp={}", receiptRequest.getOtp());
                throw new ApplicationException(ResponseString.VOUCHER_NOT_FOUND, ErrorCode.VOUCHER_NOT_FOUND);
            }
            EVoucher voucher = voucherRepository.findById(voucherId)
                    .orElseThrow(() -> new ApplicationException(ResponseString.VOUCHER_NOT_FOUND, ErrorCode.VOUCHER_NOT_FOUND));
            log.info("[Confirm voucher] Found voucher ev={}", voucherId);

            if (!VoucherStatusCode.DISABLED.equals(voucher.getVoucherStatusCode())) {
                log.error("[Confirm voucher] Voucher isn't disabled ev={}", voucherId);
                throw new ApplicationException(ResponseString.VOUCHER_CANNOT_RECEIPT, ErrorCode.VOUCHER_CANNOT_RECEIPT);
            }
            if (!voucher.getTransferStatusCode().equals(TransferStatusCode.RECPT_WAIT)) {
                log.error("[Confirm voucher] Voucher isn't disabled ev={}", voucherId);
                throw new ApplicationException(ResponseString.VOUCHER_NOT_WAITING_RECEIPT, ErrorCode.VOUCHER_NOT_WAITING_RECEIPT);
            }
            //Check voucher expire
            if (voucher.getExpirationDate().before(new Date())) {
                log.error("[Confirm voucher] Voucher is expired ev={}", voucherId);
                throw new ApplicationException(ResponseString.VOUCHER_IS_EXPIRE, ErrorCode.VOUCHER_IS_EXPIRE);
            }

            VoucherTransferHistory transferHistory = transferHistoryRepository.findByToEv(voucherId);
            //15. forward checked info
            PublishReceiptVoucher receiptMessage = createPublishReceiptVoucher(transferHistory, receiptRequest);

            ResponseData<?> responseData = beConnector.confirmReceive(receiptMessage);
            if (responseData.getCode() != Constant.BE_SUCCESS_CODE) {
                throw new ApplicationException(responseData.getMessage(), responseData.getCode());
            }

            log.info("[Confirm voucher] sent to queue={}", receiptMessage);
            return voucherResponseGenerator.getVoucherResponse(voucher);
        } catch (ApplicationException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(e.getMessage(), ErrorCode.UNKNOWN_ERROR);
        }
    }

    @Override
    public PageResponse<VoucherResponse> getVoucherListByPhoneNo(UserVoucherListRequest request) {
        try {
            String encryptedNumber = request.getMobileNumber();
            log.info("[Get voucher list by phone no] Start req={}", encryptedNumber);
            String mobileNumber = encryption.decryptData(encryptedNumber);
            if (Objects.isNull(mobileNumber)) {
                log.error("[Get voucher list by phone no] Invalid user mobile number");
                throw new ApplicationException(ResponseString.VOUCHER_NOT_FOUND, ErrorCode.VOUCHER_NOT_FOUND);
            }
            // Get list voucher of user
            PageResponse<VoucherResponse> userVoucherListResponse = new PageResponse<>();
            Pageable pageable = PageRequest.of(request.getPageNum(), request.getPageSize(), Sort.by("creationDate").descending());
            Page<EVoucher> vouchersPage = voucherRepository.findAllByUserMobileNumberStartingWith(encryptedNumber, pageable);
            userVoucherListResponse.setTotalCount(vouchersPage.getTotalPages());
            userVoucherListResponse.setPageData(vouchersPage.get().map(voucherResponseGenerator::getVoucherResponse).collect(Collectors.toList()));
            userVoucherListResponse.setPageSize(request.getPageSize());
            userVoucherListResponse.setPageNum(request.getPageNum());
            userVoucherListResponse.setStatus("Success");
            return userVoucherListResponse;
        } catch (ApplicationException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(e.getMessage(), ErrorCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 3.2.1 EVoucher consuming for an end user
     *
     * @param id String
     * @return UserVoucherDetailResponse
     */
    @Override
    public VoucherResponseWrapper getVoucherDetails(String id) {
        return baseVoucherService.getVoucherDetails(id);
    }

    /**
     * 3.2.5 EVoucher handover
     *
     * @param transferVoucherRequest TransferVoucherRequest
     * @return VoucherModel
     */
    @Override
    public VoucherResponse transferVoucher(TransferVoucherRequest transferVoucherRequest) {
        try {
            log.info("[Transfer voucher] req={}", transferVoucherRequest);
            VoucherUuidResponse uuidResponse = otpConnector.getUuidByOtp(transferVoucherRequest.getOtp());
            if (Objects.nonNull(uuidResponse.getError()) && !uuidResponse.getError().isEmpty()) {
                throw new ApplicationException(ResponseString.OTP_INVALID_OR_EXPIRE, ErrorCode.OTP_INVALID_OR_EXPIRE);
            }
            //11. inquiry voucher info
            String voucherId = uuidResponse.getData().getUuid();
            if (Objects.isNull(voucherId) || voucherId.isEmpty()) {
                log.info("[Transfer voucher] Can not get voucherId from OTP Service UseAPI, otp={}", transferVoucherRequest.getOtp());
                throw new ApplicationException(ResponseString.OTP_INVALID_OR_EXPIRE, ErrorCode.OTP_INVALID_OR_EXPIRE);
            }

            EVoucher voucher = voucherRepository.findById(voucherId)
                    .orElseThrow(() -> new ApplicationException(ResponseString.VOUCHER_NOT_FOUND, ErrorCode.VOUCHER_NOT_FOUND));

            String decryptedOldNumber = encryption.decryptData(voucher.getUserMobileNumber());
            throwIfNumberAreTheSame(decryptedOldNumber, transferVoucherRequest.getTo());
            throwIfTransferChoiceVoucher(voucher);
            throwIfVoucherIsExpired(voucher);
            throwIfVoucherIsNotInUse(voucher);
            throwIfVoucherIsTransferring(voucher);
            throwIfVoucherBalanceIsLessThan50Percent(voucher);

            log.info("[Transfer voucher] start transferring voucher={}, sender={}, receiver={}",
                    voucher.getEV(), decryptedOldNumber, transferVoucherRequest.getTo());

            String encryptedReceiverPhone = encryption.encryptData(transferVoucherRequest.getTo());
            PublishTransferVoucher publishTransferVoucher = new PublishTransferVoucher(
                    voucherId, transferVoucherRequest.getMessage(),
                    new EndUserBERequest(encryptedReceiverPhone, transferVoucherRequest.getToName()));
            log.info("[Transfer voucher] send transfer request to PS, data={}", publishTransferVoucher);

            ResponseData<?> responseData = psConnector.transfer(publishTransferVoucher);
            if (responseData.getCode() != Constant.BE_SUCCESS_CODE) {
                throw new ApplicationException(responseData.getMessage(), responseData.getCode());
            }

            log.info("[Transfer voucher] transfer voucher successfully req={}", transferVoucherRequest);
            return voucherResponseGenerator.getVoucherResponse(voucher);
        } catch (ApplicationException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(e.getMessage(), ErrorCode.UNKNOWN_ERROR);
        }
    }

    private static void throwIfVoucherBalanceIsLessThan50Percent(EVoucher voucher) {
        if (VoucherTypeCode.PP.equals(voucher.getVoucherTypeCode())
                && VoucherStatusCode.PART_USED.equals(voucher.getVoucherStatusCode())
                && voucher.getBalance() * 2 < voucher.getInitAmount()) {
            throw new ApplicationException(ResponseString.VOUCHER_REMAINING_SMALL, ErrorCode.VOUCHER_REMAINING_SMALL);
        }
    }

    private static void throwIfVoucherIsTransferring(EVoucher voucher) {
        if (TransferStatusCode.TRANSFER.equals(voucher.getTransferStatusCode())) {
            throw new ApplicationException(ResponseString.VOUCHER_TRANSFER_PROCESSING, ErrorCode.VOUCHER_TRANSFER_PROCESSING);
        }
    }

    private static void throwIfVoucherIsNotInUse(EVoucher voucher) {
        if (!VoucherStatusCode.NORMAL.equals(voucher.getVoucherStatusCode())
                && !VoucherStatusCode.PART_USED.equals(voucher.getVoucherStatusCode())) {
            throw new ApplicationException(ResponseString.INVALID_VOUCHER_STATUS, ErrorCode.INVALID_VOUCHER_STATUS);
        }
    }

    private static void throwIfVoucherIsExpired(EVoucher voucher) {
        if (voucher.getVoucherStatusCode().equals(VoucherStatusCode.EXPIRE)
                || voucher.getExpirationDate().before(new Date())) {
            log.error("[Transfer voucher] voucher is already expired, ev={}", voucher.getEV());
            throw new ApplicationException(ResponseString.VOUCHER_IS_EXPIRE, ErrorCode.VOUCHER_IS_EXPIRE);
        }
    }

    private void throwIfTransferChoiceVoucher(EVoucher voucher) {
        if (EnumSet.of(SystemType.CHOICE, SystemType.BULK).contains(voucher.getSystem())) {
            log.error("[Transfer voucher] choice voucher can not be transferred");
            throw new ApplicationException(ResponseString.CAN_NOT_TRANSFER_CHOICE_VOUCHER, ErrorCode.CAN_NOT_TRANSFER_CHOICE_VOUCHER);
        }
    }

    @Override
    public Boolean checkVoucherExistForPhoneNumber(String phoneNumberEncrypt) {
        try {
            log.info("Check Voucher exist with phoneNumberEncrypt: {}", phoneNumberEncrypt);
            int total = _elasticVoucherRepository.getCountUserVoucher(encryption.decryptData(phoneNumberEncrypt));
            return total > 0;
        } catch (Exception e) {
            log.error("Error Check Voucher exist with phoneNumberEncrypt: {}", e.getMessage(), e);
            throw new ApplicationException(e.getMessage(), ErrorCode.UNKNOWN_ERROR);
        }
    }

    @Override
    public OtpData getOtpByPhoneNumber(String phoneNumberEncrypt) {
        return baseVoucherService.getOtpByPhoneNumber(phoneNumberEncrypt);
    }

    @Override
    public void createOtpToGetVoucherList(String phoneNumberEncrypt) {
        baseVoucherService.createOtp(phoneNumberEncrypt);
    }

    @Override
    public void activate(ActivateRequestV1 activateReq) throws InvalidException {
        validateActivateReq(activateReq);

        EVoucher voucher = voucherRepository.findBySerialNo(activateReq.getSerialNumber())
                .orElseThrow(() -> new ApplicationException("Can not find voucher by serial number=" + activateReq.getSerialNumber(),
                        ErrorCode.VOUCHER_NOT_FOUND_BY_SERIAL_NO));

        if (Objects.isNull(voucher.getActivationUrl())
                || voucher.getActivationUrl().isEmpty()
                || !voucher.getActivationUrl().endsWith(activateReq.getActivationKey())) {
            throw new ApplicationException("Activation key mismatch", ErrorCode.ACTIVATION_KEY_MISMATCH);
        }

        if (VoucherUtils.isActivated(voucher)) {
            throw new ApplicationException("Voucher is already activated", ErrorCode.VOUCHER_ALREADY_ACTIVATED);
        }

        if (VoucherStatusCode.NORMAL != voucher.getVoucherStatusCode()) {
            throw new ApplicationException("Voucher has invalid status " + voucher.getVoucherStatusCode(), ErrorCode.VOUCHER_ALREADY_ACTIVATED);
        }
        // Call FE API to activate the voucher
        try {
            activateReq.setEv(voucher.getEV());
            feConnector.activateVoucher(activateReq);
        } catch (ApplicationException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Error calling API", e);
            throw new ApplicationException(
                    "Error calling API",
                    ErrorCode.CAN_NOT_ACTIVATE_VOUCHER);
        }
    }

    @Override
    public VoucherModel voucherBySerialNo(String serialNumber) throws ApplicationException {
        VoucherModel voucherModel = _voucherEsRepository.findBySerialNo(serialNumber);
        if (Objects.isNull(voucherModel)) {
            throw new ApplicationException("Can not find voucher, sNo=" + serialNumber, ErrorCode.VOUCHER_NOT_FOUND_BY_SERIAL_NO);
        }
        if (VoucherUtils.isActivated(voucherModel)) {
            throw new ApplicationException("Voucher is already activated, sNo=" + serialNumber, ErrorCode.VOUCHER_ALREADY_ACTIVATED);
        }
        return voucherModel;
    }

    private void validateActivateReq(ActivateRequestV1 activateReq) throws InvalidException {
        if (Objects.isNull(activateReq)) {
            throw new InvalidException("Null request");
        }
        if (activateReq.getUserName().isEmpty()) {
            throw new InvalidException("Empty user name");
        }
        if (activateReq.getPhoneNumber().isEmpty()) {
            throw new InvalidException("Empty phone number");
        }
        if (activateReq.getSerialNumber().isEmpty()) {
            throw new InvalidException("Empty serial number");
        }
        if (activateReq.getActivationKey().isEmpty()) {
            throw new InvalidException("Empty activationId");
        }

    }

    @Override
    public List<VoucherResponse> getVoucherListByEv(String ev, SystemType systemType) {
        log.info("Get {} children of voucher={}", systemType, ev);
        try {
            List<EVoucher> vouchers = voucherRepository
                    .findAllByParentVoucherEvOrderByCreationDateDesc(ev);
            log.info("found: {}", vouchers);

            if (vouchers.isEmpty()) {
                log.error("Can not find voucher with parent id: {}", ev);
                return new ArrayList<>();
            }
            return vouchers.stream().map(voucherResponseGenerator::getVoucherResponse).collect(Collectors.toList());
        } catch (ApplicationException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(e.getMessage(), ErrorCode.UNKNOWN_ERROR);
        }
    }

    private PublishReceiptVoucher createPublishReceiptVoucher(VoucherTransferHistory history, ReceiptRequest receiptRequest) {
        PublishReceiptVoucher receiptVoucher = new PublishReceiptVoucher();
        receiptVoucher.setTransferStatusCode(EnumVoucherTransferStatus.RECPTED);
        receiptVoucher.setTransactionDate(DateUtils.toDateTimeString(history.getTransactionDate()));
        receiptVoucher.setReceiptConfirmDate(DateUtils.getCurrentDateTimeString());
        receiptVoucher.setReturnDate(DateUtils.toDateTimeString(history.getReturnDate()));
        receiptVoucher.setFromVoucherShortLink(history.getFromVoucherShortLink());
        receiptVoucher.setFromMobileNumber(history.getFromMobileNumber());
        receiptVoucher.setFromEv(history.getFromEv());
        receiptVoucher.setToMobileNumber(history.getToVoucherShortLink());
        receiptVoucher.setToMobileNumber(history.getToMobileNumber());
        receiptVoucher.setToEv(history.getToEv());
        receiptVoucher.setVoucherTypeCode(history.getVoucherTypeCode());
        receiptVoucher.setInitAmount(history.getInitAmount());
        receiptVoucher.setTransferAmount(history.getTransferAmount());

        // Information of voucher recipient
        var endUserReceipt = EndUserBERequest.builder()
                .userMobileNum(receiptVoucher.getToMobileNumber())
                .userNm(receiptRequest.getName())
                .gender(receiptRequest.getGender())
                .birthday(receiptRequest.getBirthday())
                .address(receiptRequest.getDistrict() + ", " + receiptRequest.getProvince())
                .build();
        receiptVoucher.setEndUser(endUserReceipt);
        return receiptVoucher;
    }

    private void throwIfNumberAreTheSame(String oldNumber, String newNumber) {
        if (oldNumber.equals(newNumber)) {
            throw new ApplicationException(ResponseString.DUPLICATE_TRANSFER_MOBILE_NUMBER, ErrorCode.DUPLICATE_TRANSFER_MOBILE_NUMBER);
        }
    }


}
