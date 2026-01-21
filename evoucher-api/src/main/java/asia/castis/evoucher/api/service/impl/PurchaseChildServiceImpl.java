package asia.castis.evoucher.api.service.impl;

import asia.castis.evoucher.api.common.enums.VoucherStatusCode;
import asia.castis.evoucher.api.dto.request.choose.ChosenRequest;
import asia.castis.evoucher.api.dto.request.choose.ChosenRequestV1;
import asia.castis.evoucher.api.dto.request.choose.ChosenRequestV2;
import asia.castis.evoucher.api.dto.response.ResponseData;
import asia.castis.evoucher.api.entity.EVoucher;
import asia.castis.evoucher.api.entity.Goods;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.repository.GoodsRepository;
import asia.castis.evoucher.api.repository.VoucherRepository;
import asia.castis.evoucher.api.service.LockService;
import asia.castis.evoucher.api.service.PsConnector;
import asia.castis.evoucher.api.service.PurchaseChildService;
import asia.castis.evoucher.api.service.base.BaseVoucherService;
import asia.castis.evoucher.api.utils.ErrorCode;
import asia.castis.evoucher.api.utils.ResponseString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;

import static asia.castis.evoucher.api.common.Constant.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PurchaseChildServiceImpl<T extends ChosenRequest> implements PurchaseChildService<T> {
    private final VoucherRepository voucherRepository;
    private final GoodsRepository goodsRepository;
    private final LockService lockService;
    private final PsConnector psConnector;
    private final BaseVoucherService baseVoucherService;

    public void chooseProduct(T chosenRequest) throws ApplicationException {
        try {
            log.info("create child items: {}", gson.toJson(chosenRequest));
            EVoucher parentVoucher = voucherRepository
                    .findById(chosenRequest.getParentVoucherId())
                    .orElseThrow(() -> new ApplicationException(ResponseString.VOUCHER_NOT_FOUND, ErrorCode.VOUCHER_NOT_FOUND));

            validateRequest(chosenRequest, parentVoucher);
            ResponseData<?> responseData;
            if (chosenRequest instanceof ChosenRequestV2) {
                ChosenRequestV2 chosenRequestV2 = (ChosenRequestV2) chosenRequest;
                baseVoucherService.validateOtp(parentVoucher.getUserMobileNumber() ,chosenRequestV2.getOtp());

                responseData = psConnector.chooseChoiceV2(chosenRequestV2);
            } else {
                responseData = psConnector.chooseChoice((ChosenRequestV1) chosenRequest);
            }

            if (responseData.getCode() != BE_SUCCESS_CODE) {
                throw new ApplicationException(responseData.getMessage(), responseData.getCode());
            }
            log.info("Create choice vouchers successfully: {}", chosenRequest);
        } catch (ApplicationException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Error when create choice items: {}", e.getMessage(), e);
            throw new ApplicationException(
                    e.getMessage(),
                    ErrorCode.CAN_NOT_CREATE_CHOICE_VOUCHER);
        }
    }

    private void validateRequest(@Valid T chosenRequest, EVoucher voucher) {
        if (Objects.isNull(chosenRequest.getType())) {
            log.error("Parent type is missing");
            throw new ApplicationException(ResponseString.INVALID_VOUCHER_STATUS, ErrorCode.INVALID_VOUCHER_STATUS);
        }
        if (!EnumSet.of(VoucherStatusCode.NORMAL, VoucherStatusCode.PART_USED).contains(voucher.getVoucherStatusCode())) {
            log.error("Voucher status is invalid: {}", voucher.getVoucherStatusCode());
            throw new ApplicationException(ResponseString.INVALID_VOUCHER_STATUS, ErrorCode.INVALID_VOUCHER_STATUS);
        }

        if (lockService.getPurchaseChoiceLocker(chosenRequest.getParentVoucherId()).isLocked()) {
            throw new ApplicationException(ResponseString.VOUCHER_IS_BEING_PROCESSED, ErrorCode.VOUCHER_IS_BEING_PROCESSED);
        }

        if (chosenRequest instanceof ChosenRequestV1) {
            ChosenRequestV1 chosenRequestV1 = (ChosenRequestV1) chosenRequest;
            if (!chosenRequestV1.getToken().equals(voucher.getParentVoucherToken())) {
                throw new ApplicationException(ResponseString.OTP_MISMATCH, ErrorCode.OTP_MISMATCH);
            }
        }

        double usedAmount = chosenRequest.getProducts().stream()
                .mapToDouble(prod -> {
                    Optional<Goods> goods = goodsRepository.findById(prod.getGoodsId());
                    return goods.map(value -> value.getSellPrice() * prod.getQuantity()).orElse(0.0);
                })
                .sum();
        log.info("Total used amount={}, current balance={} ", usedAmount, voucher.getBalance());

        if (usedAmount > voucher.getBalance()) {
            log.error("Total used amount {} > {} balance", usedAmount, voucher.getBalance());
            throw new ApplicationException(ResponseString.VOUCHER_LIMIT_AMOUNT, ErrorCode.VOUCHER_LIMIT_AMOUNT);
        }
    }
}
