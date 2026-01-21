package asia.castis.evoucher.api.service.impl;

import asia.castis.evoucher.api.dto.request.PaymentHistory;
import asia.castis.evoucher.api.dto.request.activate.ActivateRequestV2;
import asia.castis.evoucher.api.dto.response.ResponseData;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.publishrequest.PublishReceiptVoucher;
import asia.castis.evoucher.api.service.BeConnector;
import asia.castis.evoucher.api.utils.ErrorCode;
import asia.castis.evoucher.api.utils.ResponseString;
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

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static asia.castis.evoucher.api.common.Constant.gson;

@Service
@Slf4j
@RequiredArgsConstructor
public class BeConnectorImpl implements BeConnector {

    @Value("${evoucher.service-be.url}")
    private String serviceBeUrl;
    private final RestTemplate restTemplate;

    @Override
    public ResponseData<?> purchase(PaymentHistory paymentRequest) {
        return postRequest(Strings.concat(serviceBeUrl, "/evouchers/use-voucher"), paymentRequest);
    }

    @Override
    public ResponseData<?> cancelExchange(PaymentHistory cancelRequest) {
        return postRequest(Strings.concat(serviceBeUrl, "/evouchers/cancel-exchange"), cancelRequest);
    }

    @Override
    public ResponseData<?> confirmReceive(PublishReceiptVoucher cancelRequest) {
        return postRequest(Strings.concat(serviceBeUrl, "/evouchers/receive"), cancelRequest);
    }

    @Override
    public ResponseData<Map<Long, Integer>> getRemainingCount(List<Long> goodsId) throws URISyntaxException {
        StringBuilder goodsIdJoin = new StringBuilder();
        goodsId.forEach(o -> goodsIdJoin.append(o).append(","));
        String requestUrl = String.format("%s/%s?ids=%s", serviceBeUrl, "pins/quantity", goodsIdJoin.substring(0, goodsIdJoin.length() - 1));

        return getRequest(requestUrl, new ParameterizedTypeReference<>() {
        });
    }

    @Override
    public ResponseData<?> activate(ActivateRequestV2 activateRequest) {
        return postRequest(Strings.concat(serviceBeUrl, "/evouchers/activate-v2"), activateRequest);
    }


    public <T> T getRequest(String url, ParameterizedTypeReference<T> typeReference) {
        try {
            log.info("[BE Connector] GET request with URL: {}", url);
            ResponseEntity<T> response = restTemplate.exchange(url,
                    HttpMethod.GET,
                    null,
                    typeReference);
            log.info("[BE Connector] GET response: code={}, body={}", response.getStatusCodeValue(), gson.toJson(response.getBody()));
            return response.getBody();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            log.error("[BE Connector] POST Error response EVoucher service BE: {}", e.getMessage(), e);
            throw gson.fromJson(e.getResponseBodyAsString(), ApplicationException.class);
        } catch (Exception e) {
            log.error(String.format("[BE Connector] GET Error: %s", e.getMessage()), e);
            throw new ApplicationException(ResponseString.EXCEPTION_WHILE_REQUESTING_TO_BE, ErrorCode.EXCEPTION_WHILE_REQUESTING_TO_BE);
        }
    }
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
            throw new ApplicationException(e.getMessage(), ErrorCode.EXCEPTION_WHILE_REQUESTING_TO_BE);
        }
    }

}
