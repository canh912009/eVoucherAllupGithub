package asia.castis.evoucher.api.service;

import asia.castis.evoucher.api.dto.request.choose.ChosenRequest;
import asia.castis.evoucher.api.exception.ApplicationException;

import javax.validation.Valid;

public interface PurchaseChildService<T extends ChosenRequest> {
    void chooseProduct(@Valid T chosenRequest) throws ApplicationException;
}
