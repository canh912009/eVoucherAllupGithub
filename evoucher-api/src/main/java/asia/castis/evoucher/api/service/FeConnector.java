package asia.castis.evoucher.api.service;

import asia.castis.evoucher.api.dto.otpservice.restore.VoucherOtpResponse;
import asia.castis.evoucher.api.dto.request.activate.ActivateRequestV1;
import asia.castis.evoucher.api.dto.request.activate.ActivateRequestV2;
import asia.castis.evoucher.api.dto.request.choose.ChosenRequestV1;
import asia.castis.evoucher.api.dto.request.choose.ChosenRequestV2;
import asia.castis.evoucher.api.dto.response.ResponseData;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface FeConnector {
    ResponseData<List<String>> callAPICreateChildOfChoiceVoucher(ChosenRequestV1 request);

    ResponseData<List<String>> callAPICreateChildOfChoiceVoucherV2(ChosenRequestV2 request);

    ResponseData<Object> sendOptVoucher(String phoneNumberEncrypt, VoucherOtpResponse otpResponse) throws URISyntaxException, IOException, InterruptedException;

    void activateVoucher(ActivateRequestV1 activateReq);

    void activateVoucherV2(ActivateRequestV2 activateReq);
}
