package asia.castis.evoucher.api.service.impl;

import asia.castis.evoucher.api.common.enums.SystemType;
import asia.castis.evoucher.api.dto.request.choose.*;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.service.ChoiceVoucherService;
import asia.castis.evoucher.api.service.PurchaseChildService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static asia.castis.evoucher.api.common.Constant.gson;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChoiceVoucherServiceImpl<T extends ChoiceChosenRequest, R extends ChosenRequest> implements ChoiceVoucherService<T> {
    private final PurchaseChildService<R> purchaseChildService;

    private R convertToChosenRequest(T choiceReq) {
        if (choiceReq instanceof ChoiceChosenRequestV1) {
            ChoiceChosenRequestV1 choiceReqV1 = (ChoiceChosenRequestV1) choiceReq;
            ChosenRequestV1 chosenReq = ChosenRequestV1.builder()
                    .parentVoucherId(choiceReq.getChoiceVoucherId())
                    .token(choiceReqV1.getToken())
                    .products(choiceReq.getChoices())
                    .type(SystemType.CHOICE.name())
                    .build();
            return (R) chosenReq;
        }
        ChoiceChosenRequestV2 choiceReqV2 = (ChoiceChosenRequestV2) choiceReq;
        ChosenRequestV2 chosenReq = ChosenRequestV2.builder()
                .parentVoucherId(choiceReq.getChoiceVoucherId())
                .otp(choiceReqV2.getOtp())
                .products(choiceReq.getChoices())
                .type(SystemType.CHOICE.name())
                .build();
        return (R) chosenReq;
    }

    @Override
    public void chooseChoiceVoucher(T choiceRequest) throws ApplicationException {
        log.info("Choose choice product: {}", gson.toJson(choiceRequest));
        purchaseChildService.chooseProduct(convertToChosenRequest(choiceRequest));
    }
}
