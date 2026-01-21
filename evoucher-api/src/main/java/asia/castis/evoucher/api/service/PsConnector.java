package asia.castis.evoucher.api.service;

import asia.castis.evoucher.api.dto.request.choose.ChoiceChosenRequestV1;
import asia.castis.evoucher.api.dto.request.choose.ChoiceChosenRequestV2;
import asia.castis.evoucher.api.dto.request.choose.ChosenRequestV1;
import asia.castis.evoucher.api.dto.request.choose.ChosenRequestV2;
import asia.castis.evoucher.api.dto.response.ResponseData;
import asia.castis.evoucher.api.publishrequest.PublishTransferVoucher;

public interface PsConnector {
    ResponseData<?> transfer(PublishTransferVoucher request);

    ResponseData<?> chooseChoiceV2(ChosenRequestV2 choiceRequest);

    ResponseData<?> chooseChoice(ChosenRequestV1 choiceRequest);
}
