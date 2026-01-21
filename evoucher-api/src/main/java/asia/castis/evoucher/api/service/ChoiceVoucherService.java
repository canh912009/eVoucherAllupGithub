package asia.castis.evoucher.api.service;

import asia.castis.evoucher.api.dto.request.choose.ChoiceChosenRequest;
import asia.castis.evoucher.api.dto.request.choose.ChoiceChosenRequestV1;
import asia.castis.evoucher.api.dto.request.choose.ChosenRequest;
import asia.castis.evoucher.api.exception.ApplicationException;

public interface ChoiceVoucherService<T extends ChoiceChosenRequest> {
    void chooseChoiceVoucher(T choiceRequest) throws ApplicationException;
}
