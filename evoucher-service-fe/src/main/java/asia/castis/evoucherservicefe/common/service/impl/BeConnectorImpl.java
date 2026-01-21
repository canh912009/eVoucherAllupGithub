package asia.castis.evoucherservicefe.common.service.impl;

import asia.castis.evoucherservicefe.common.dto.response.ResponseData;
import asia.castis.evoucherservicefe.common.service.BeConnector;
import asia.castis.evoucherservicefe.exceptions.ApplicationException;
import asia.castis.evoucherservicefe.voucherhandler.dto.ActivateRequest;
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

import static asia.castis.evoucherservicefe.common.utils.Const.gson;

@Service
@Slf4j
@RequiredArgsConstructor
public class BeConnectorImpl implements BeConnector {

    private static final int EXCEPTION_WHILE_REQUESTING_TO_BE = 1210;
    @Value("${evoucher.service-be.url}")
    private String serviceBeUrl;
    private final RestTemplate restTemplate;

    public <T, R> ResponseData<R> postRequest(String url, T requestBody) {
        try {
            log.info("[BE Connector] POST request with URL: {}", url);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<T> entity = new HttpEntity<>(requestBody, headers);

            log.info("[BE Connector] POST request with payload: {}", gson.toJson(requestBody));
            ResponseEntity<ResponseData<R>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            entity,
                            new ParameterizedTypeReference<>() {
                            });
            log.info("[BE Connector] POST response: code={}, body={}", response.getStatusCodeValue(), gson.toJson(response.getBody()));
            return response.getBody();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            log.error("[BE Connector] POST Error response EVoucher service BE: {}", e.getMessage(), e);
            throw gson.fromJson(e.getResponseBodyAsString(), ApplicationException.class);
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage(), EXCEPTION_WHILE_REQUESTING_TO_BE);
        }
    }

    @Override
    public ResponseData<?> activate(ActivateRequest activateRequest) {
        return postRequest(Strings.concat(serviceBeUrl, "/evouchers/activate"), activateRequest);
    }
}
