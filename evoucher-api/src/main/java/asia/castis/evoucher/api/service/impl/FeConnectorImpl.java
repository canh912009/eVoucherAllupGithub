package asia.castis.evoucher.api.service.impl;

import asia.castis.evoucher.api.dto.otpservice.restore.VoucherOtpResponse;
import asia.castis.evoucher.api.dto.request.activate.ActivateRequestV1;
import asia.castis.evoucher.api.dto.request.SendOtpVoucherRequest;
import asia.castis.evoucher.api.dto.request.activate.ActivateRequestV2;
import asia.castis.evoucher.api.dto.request.choose.ChosenRequestV1;
import asia.castis.evoucher.api.dto.request.choose.ChosenRequestV2;
import asia.castis.evoucher.api.dto.response.ResponseData;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.service.FeConnector;
import asia.castis.evoucher.api.utils.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static asia.castis.evoucher.api.common.Constant.gson;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeConnectorImpl implements FeConnector {

    @Value("${evoucher.service-fe.url}")
    private String serviceFeUrl;
    private final RestTemplate restTemplate;

    @Override
    public ResponseData<List<String>> callAPICreateChildOfChoiceVoucher(ChosenRequestV1 request) {
        return postRequest(serviceFeUrl + "/voucher/choose-choice", request);
    }

    @Override
    public ResponseData<List<String>> callAPICreateChildOfChoiceVoucherV2(ChosenRequestV2 request) {
        return postRequest(serviceFeUrl + "/v2/voucher/choose-choice", request);
    }

    @Override
    public ResponseData<Object> sendOptVoucher(String phoneNumberEncrypt, VoucherOtpResponse otpResponse) {
        var requestEntity = SendOtpVoucherRequest
                .builder()
                .phoneNumber(phoneNumberEncrypt)
                .otp(otpResponse.getData().getOtp())
                .build();
        return postRequest(serviceFeUrl + "/otp-sms", requestEntity);
    }

    @Override
    public void activateVoucher(ActivateRequestV1 activateReq) {
        postRequest(serviceFeUrl + "/voucher/activate", activateReq);
    }

    @Override
    public void activateVoucherV2(ActivateRequestV2 activateReq) {
        postRequest(serviceFeUrl + "/voucher/v2/activate", activateReq);
    }

    public <T, R> ResponseData<R> postRequest(String url, T requestBody) {
        try {
            log.info("[FE Connector] POST request with URL: {}", url);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<T> entity = new HttpEntity<>(requestBody, headers);

            log.info("[FE Connector] POST request with payload: {}", gson.toJson(requestBody));
            ResponseEntity<ResponseData<R>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            entity,
                            new ParameterizedTypeReference<>() {
                            });
            log.info("[FE Connector] POST response: code={}, body={}", response.getStatusCodeValue(), gson.toJson(response.getBody()));
            return response.getBody();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            log.error("[FE Connector] POST Error response EVoucher service FE: {}", e.getMessage(), e);
            throw gson.fromJson(e.getResponseBodyAsString(), ApplicationException.class);
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage(), ErrorCode.EXCEPTION_WHILE_REQUESTING_FE);
        }
    }
}
