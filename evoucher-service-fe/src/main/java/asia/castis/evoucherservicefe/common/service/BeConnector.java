package asia.castis.evoucherservicefe.common.service;


import asia.castis.evoucherservicefe.common.dto.response.ResponseData;
import asia.castis.evoucherservicefe.voucherhandler.dto.ActivateRequest;

public interface BeConnector {
    ResponseData<?> activate(ActivateRequest activateRequest);
}
