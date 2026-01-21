package asia.castis.evoucher.api.service.impl;

import asia.castis.evoucher.api.common.Constant;
import asia.castis.evoucher.api.common.enums.VnptCardAction;
import asia.castis.evoucher.api.common.enums.VoucherStatusCode;
import asia.castis.evoucher.api.dto.request.PaymentHistory;
import asia.castis.evoucher.api.dto.request.vnpt.VnptPurchaseRequest;
import asia.castis.evoucher.api.dto.response.ResponseData;
import asia.castis.evoucher.api.entity.EVoucher;
import asia.castis.evoucher.api.entity.Goods;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.repository.GoodsRepository;
import asia.castis.evoucher.api.repository.VoucherRepository;
import asia.castis.evoucher.api.service.*;
import asia.castis.evoucher.api.service.generator.PaymentHistoryGenerator;
import asia.castis.evoucher.api.utils.ErrorCode;
import asia.castis.evoucher.api.utils.ResponseString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("vnptVoucherService")
@Slf4j
@RequiredArgsConstructor
public class VnptVoucherServiceImpl implements VnptVoucherService {
    public static final int SUCCESS_CODE = 0;
    public static final int PENDING_CODE = 99;

    private final VoucherRepository voucherRepository;
    private final LockService lockService;
    private final BeConnector beConnector;
    private final GoodsRepository goodsRepository;
    private final PaymentHistoryGenerator historyGenerator;

    public boolean isSuccess(int returnCode) {
        return returnCode == SUCCESS_CODE;
    }

    public boolean isPending(int returnCode) {
        return returnCode == PENDING_CODE;
    }

    @Override
    public void purchase(VnptPurchaseRequest purchaseRequest) {
        try {
            log.info("Purchasing from VNPT Epay voucher: {}", purchaseRequest);
            EVoucher parentVoucher = voucherRepository
                    .findById(purchaseRequest.getEv())
                    .orElseThrow(() -> new ApplicationException(ResponseString.VOUCHER_NOT_FOUND, ErrorCode.VOUCHER_NOT_FOUND));

            // Check if balance is less than initAmount (~ voucher VNPT is pending)
            if (parentVoucher.getBalance() < parentVoucher.getInitAmount()) {
                log.error("Cannot purchase voucher: {}, balance {} is less than initial amount {}",
                        parentVoucher.getEV(), parentVoucher.getBalance(), parentVoucher.getInitAmount());
                throw new ApplicationException(ResponseString.VNPT_IS_STILL_IN_PROGRESS, ErrorCode.VNPT_IS_STILL_IN_PROGRESS);
            }

            Goods goods = goodsRepository
                    .findById(parentVoucher.getGoodsId())
                    .orElseThrow(() -> new ApplicationException(ResponseString.CAN_NOT_FIND_GOODS, ErrorCode.CAN_NOT_FIND_GOODS));

            validateRequest(purchaseRequest, parentVoucher);
            PaymentHistory vnptPaymentHistory = historyGenerator.getVnptPaymentHistory(parentVoucher, goods, purchaseRequest);
            ResponseData<?> responseData = beConnector.purchase(vnptPaymentHistory);

            if (isPending(responseData.getCode())) {
                log.error("Cannot purchase voucher: {}, VNPT is still in progress", parentVoucher.getEV());
                throw new ApplicationException(ResponseString.VNPT_IS_STILL_IN_PROGRESS, ErrorCode.VNPT_IS_STILL_IN_PROGRESS);
            }
            if (!isSuccess(responseData.getCode())) {
                throw new ApplicationException(responseData.getMessage(), responseData.getCode());
            }
            log.info("Purchasing from VNPT Epay voucher successfully: {}", purchaseRequest);
        } catch (ApplicationException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Error when purchasing from VNPT Epay voucher: {}", e.getMessage(), e);
            throw new ApplicationException(
                    e.getMessage(),
                    ErrorCode.CAN_NOT_PURCHASE_FROM_VNPT_EPAY);
        }

    }

    private void validateRequest(VnptPurchaseRequest purchaseRequest, EVoucher voucher) {
        if (!VoucherStatusCode.NORMAL.equals(voucher.getVoucherStatusCode())) {
            log.error("Voucher status is invalid: {}", voucher.getVoucherStatusCode());
            throw new ApplicationException(ResponseString.INVALID_VOUCHER_STATUS, ErrorCode.INVALID_VOUCHER_STATUS);
        }
        if (VnptCardAction.TOPUP.equals(VnptCardAction.valueOf(purchaseRequest.getAction()))
                && (Objects.isNull(purchaseRequest.getReceiverPhoneNo()) || purchaseRequest.getReceiverPhoneNo().isEmpty())) {
            throw new ApplicationException("Receiver phone number is required for topup", ErrorCode.RECEIVER_PHONE_NO_REQUIRED_FOR_TOPUP);
        }

        if (lockService.getPurchaseChoiceLocker(purchaseRequest.getEv()).isLocked()) {
            throw new ApplicationException(ResponseString.VOUCHER_IS_BEING_PROCESSED, ErrorCode.VOUCHER_IS_BEING_PROCESSED);
        }
    }
}
