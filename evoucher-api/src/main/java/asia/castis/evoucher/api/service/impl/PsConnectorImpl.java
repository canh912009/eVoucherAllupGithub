package asia.castis.evoucher.api.service.impl;

import asia.castis.evoucher.api.dto.request.choose.ChosenRequestV1;
import asia.castis.evoucher.api.dto.request.choose.ChosenRequestV2;
import asia.castis.evoucher.api.dto.response.ResponseData;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.publishrequest.PublishTransferVoucher;
import asia.castis.evoucher.api.service.PsConnector;
import asia.castis.evoucher.api.utils.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static asia.castis.evoucher.api.common.Constant.gson;

@Service
@Slf4j
@RequiredArgsConstructor
public class PsConnectorImpl implements PsConnector {

    @Value("${evoucher.publish-service.url}")
    private String publishServiceUrl;
    private final RestTemplate restTemplate;

    public <T, R> ResponseData<R> postRequest(String url, T requestBody) {
        try {
            log.info("[PS Connector] POST request with URL: {}", url);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<T> entity = new HttpEntity<>(requestBody, headers);

            log.info("[PS Connector] POST request with payload: {}", gson.toJson(requestBody));
            ResponseEntity<ResponseData<R>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            entity,
                            new ParameterizedTypeReference<>() {
                            });
            log.info("[PS Connector] POST response: code={}, body={}", response.getStatusCodeValue(), gson.toJson(response.getBody()));
            return response.getBody();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            log.error("[PS Connector] POST Error response EVoucher service PS: {}", e.getMessage());
            throw gson.fromJson(e.getResponseBodyAsString(), ApplicationException.class);
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage(), ErrorCode.EXCEPTION_WHILE_REQUESTING_TO_BE);
        }
    }

    @Override
    public ResponseData<?> transfer(PublishTransferVoucher request) {
        return postRequest(Strings.concat(publishServiceUrl, "/voucher/transfer"), request);
    }

    @Override
    public ResponseData<?> chooseChoiceV2(ChosenRequestV2 choiceRequest) {
        return postRequest(Strings.concat(publishServiceUrl, "/voucher/choose-choice-v2"), choiceRequest);
    }

    @Override
    public ResponseData<?> chooseChoice(ChosenRequestV1 choiceRequest) {
        return postRequest(Strings.concat(publishServiceUrl, "/voucher/choose-choice"), choiceRequest);
    }
}
