package asia.castis.evoucher.api.service.impl;

import asia.castis.evoucher.api.dto.otpservice.generate.OtpGenerateTTLRequest;
import asia.castis.evoucher.api.dto.otpservice.generate.OtpResponse;
import asia.castis.evoucher.api.dto.otpservice.restore.VoucherOtpResponse;
import asia.castis.evoucher.api.dto.otpservice.use.VoucherUuidResponse;
import asia.castis.evoucher.api.exception.ApplicationException;
import asia.castis.evoucher.api.service.OtpConnector;
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
import org.springframework.web.client.RestTemplate;

import static asia.castis.evoucher.api.common.Constant.gson;

@Service
@Slf4j
@RequiredArgsConstructor
public class OtpConnectorImpl implements OtpConnector {

    @Value("${evoucher.otpservice}")
    private String otpUrl;
    private final RestTemplate restTemplate;

    @Override
    public VoucherUuidResponse getUuidByOtp(String otp) {
        return getRequest(Strings.concat(otpUrl, "/otp/get/"), otp, new ParameterizedTypeReference<VoucherUuidResponse>() {
        });
    }

    @Override
    public OtpResponse getOtpByPhoneNo(String key) {
        try {
            String requestUrl = String.format("%s/otp/get-otp?key=%s", otpUrl, key);
            log.info("[OTP Connector] GET request with URL: {}", requestUrl);
            ResponseEntity<OtpResponse> response = restTemplate.getForEntity(requestUrl, OtpResponse.class);
            log.info("[OTP Connector] GET response: code={}, body={}", response.getStatusCodeValue(), gson.toJson(response.getBody()));
            return response.getBody();
            // Add catch for not found exception
        }catch (HttpClientErrorException e) {
            log.error(String.format("[OTP Connector] Client Error exception: %s", e.getMessage()));
            throw new ApplicationException(ResponseString.OTP_INVALID_OR_EXPIRE, ErrorCode.OTP_INVALID_OR_EXPIRE);
        } catch (Exception e) {
            log.error(String.format("[OTP Connector] Get Error: %s", e.getMessage()), e);
            throw new ApplicationException(ResponseString.OTP_INVALID_OR_EXPIRE, ErrorCode.OTP_INVALID_OR_EXPIRE);
        }
    }

    /**
     <prev>
     generate default OTP with 8 characters and last for 3 mins
     </prev>
     */
    @Override
    public VoucherOtpResponse generateDefaultOtp(String ev) {
        return postRequest(String.format("%s/otp/generate/%s", otpUrl, ev), null, new ParameterizedTypeReference<VoucherOtpResponse>() {
        });
    }

    /**
     <pre>
     Generate custom OTP
     ttl = time to live
     length = OTP length
     </pre>
     */
    @Override
    public VoucherOtpResponse generateOtpWithTTL(OtpGenerateTTLRequest request) {
        return postRequest(String.format("%s/otp/generateOtpTTL", otpUrl), request, new ParameterizedTypeReference<VoucherOtpResponse>() {
        });
    }

    @Override
    public VoucherUuidResponse useOtp(String otp) {
        return getRequest(String.format("%s/otp/use/", otpUrl), otp, new ParameterizedTypeReference<VoucherUuidResponse>() {
        });
    }

    @Override
    public VoucherUuidResponse getOtp(String otp) {
        return getRequest(String.format("%s/otp/get/", otpUrl), otp, new ParameterizedTypeReference<VoucherUuidResponse>() {
        });
    }

    @Override
    public void restoreOtp(String otp) {
        try {
            String url = String.format("%s/otp/restore/%s", otpUrl, otp);
            log.info("[OTP Connector] PUT request with URL: {}", url);
            restTemplate.put(url, null);
            log.info("[OTP Connector] PUT request successfully URL: {}", url);
        } catch (Exception e) {
            log.error(String.format("[OTP Connector] PUT Error: %s", e.getMessage()), e);
            throw new ApplicationException(ResponseString.OTP_INVALID_OR_EXPIRE, ErrorCode.OTP_INVALID_OR_EXPIRE);
        }
    }

    public <T, R> R postRequest(String url, T requestBody, ParameterizedTypeReference<R> typeReference) {
        try {
            log.info("[OTP Connector] POST request with URL: {}", url);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<T> entity = new HttpEntity<>(requestBody, headers);

            log.info("[OTP Connector] POST request with payload: {}", gson.toJson(requestBody));
            ResponseEntity<R> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    typeReference);
            log.info("[OTP Connector] POST response: code={}, body={}", response.getStatusCodeValue(), gson.toJson(response.getBody()));
            return response.getBody();
        } catch (Exception e) {
            log.error(String.format("[OTP Connector] POST Error: %s", e.getMessage()), e);
            throw new ApplicationException(ResponseString.OTP_INVALID_OR_EXPIRE, ErrorCode.OTP_INVALID_OR_EXPIRE);
        }
    }

    public <T> T getRequest(String url, String pathVariable, ParameterizedTypeReference<T> typeReference) {
        try {
            String requestUrl = Strings.concat(url, pathVariable);
            log.info("[OTP Connector] GET request with URL: {}", url);
            ResponseEntity<T> response = restTemplate.exchange(requestUrl,
                    HttpMethod.GET,
                    null,
                    typeReference);
            log.info("[OTP Connector] GET response: code={}, body={}", response.getStatusCodeValue(), gson.toJson(response.getBody()));
            return response.getBody();
        } catch (Exception e) {
            log.error(String.format("[OTP Connector] GET Error: %s", e.getMessage()), e);
            throw new ApplicationException(ResponseString.OTP_INVALID_OR_EXPIRE, ErrorCode.OTP_INVALID_OR_EXPIRE);
        }
    }
}
