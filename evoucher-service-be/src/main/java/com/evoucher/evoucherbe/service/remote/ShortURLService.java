package com.evoucher.evoucherbe.service.remote;

import com.evoucher.evoucherbe.dto.ShortUrlResponse;
import com.evoucher.evoucherbe.exception.CreateShortUrlException;
import com.evoucher.evoucherbe.exception.DeleteShortUrlException;
import com.evoucher.evoucherbe.utils.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShortURLService {

    public static final String OK = "OK";
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private final RestTemplate restTemplate;

    @Value("${short-link.url.request}")
    private String pathUrlRequestShortLink;
    @Value("${short-link.url.token}")
    private String shortLinkToken;
    @Value("${short-link.url.redirect}")
    private String pathUrlRedirectShortLink;
    @Value("${short-link.url.target}")
    private String shortLinkUrlTarget;
    @Value("${short-link.url.activate}")
    private String shortLinkUrlActivation;

    @Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 100))
    public String createShortURL(String targetUrl, String parameter) {
        log.info("Start creating short link for={} with parameter={}", targetUrl, parameter);
        String url = pathUrlRequestShortLink + "/link/set";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("AToken", shortLinkToken);
        body.add("L", targetUrl);
        body.add("Etc", parameter);

        log.info("Call api to get short link with data={}", Constant.gson.toJson(body));
        ResponseEntity<ShortUrlResponse> response = restTemplate.exchange(url, HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(body, headers), ShortUrlResponse.class);

        log.info("Response short link with data={}", Constant.gson.toJson(response));
        ShortUrlResponse data = response.getBody();
        if (Objects.isNull(data)
                || !OK.equals(data.getCode())
                || Objects.isNull(data.getData())) {
            throw new CreateShortUrlException("Error creating short link: " + parameter);
        }

        return pathUrlRedirectShortLink + "/" + data.getData();
    }

    public String createShortURLForEVoucher(String ev) {
        return createShortURL(shortLinkUrlTarget, ev);
    }

    public String createShortURLForActivation(String serialNo) {
        return createShortURL(shortLinkUrlActivation, serialNo);
    }
    @Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 100))
    public void deleteUrlShortLink(String shortLink) {
        log.info("Delete short link for: {}", shortLink);
        String url = pathUrlRequestShortLink + "/link/del";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("AToken", shortLinkToken);
        body.add("S", getShortValueFromUrl(shortLink));

        log.info("Call api delete short link with data: {}", Constant.gson.toJson(body));
        ResponseEntity<ShortUrlResponse> response = restTemplate.exchange(url, HttpMethod.POST,
                new HttpEntity<>(body, headers), ShortUrlResponse.class);

        log.info("Response delete short link with data: {}", Constant.gson.toJson(response));
        ShortUrlResponse data = response.getBody();
        if (Objects.isNull(data)
                || !OK.equals(data.getCode())
                || Objects.isNull(data.getData())) {
            throw new DeleteShortUrlException("Error delete short link with url: " + shortLink);
        }
    }

    public String getShortValueFromUrl(String url) {
        // Tách phần path của URL
        try {
            URI uri = new URI(url);
            String path = uri.getPath();

            // Loại bỏ ký tự "/" ở đầu nếu có
            if (path.startsWith("/")) {
                path = path.substring(1);
            }

            // Lấy chuỗi từ URL
            return path;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }
}
