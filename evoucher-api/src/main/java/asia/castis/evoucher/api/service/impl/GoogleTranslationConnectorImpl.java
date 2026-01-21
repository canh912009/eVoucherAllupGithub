package asia.castis.evoucher.api.service.impl;

import asia.castis.evoucher.api.dto.request.GoogleTranslationRequest;
import asia.castis.evoucher.api.dto.request.TranslationRequest;
import asia.castis.evoucher.api.dto.response.GoogleTranslationResponse;
import asia.castis.evoucher.api.dto.response.TranslationResponse;
import asia.castis.evoucher.api.service.TranslationConnector;
import asia.castis.evoucher.api.utils.Language;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Primary
@Slf4j
public class GoogleTranslationConnectorImpl implements TranslationConnector {

    private static final String LOG_PREFIX = "[Google Language Connector]";

    @Value("${evoucher.google.translate.url:https://translation.googleapis.com/language/translate/v2}")
    private String googleTranslateUrl;

    @Value("${evoucher.google.translate.key}")
    private String googleApiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GoogleTranslationConnectorImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public TranslationResponse translate(TranslationRequest request) {
        // Convert TranslationRequest to GoogleTranslationRequest
        GoogleTranslationRequest googleRequest = new GoogleTranslationRequest();
        googleRequest.setQ(request.getQ());
        googleRequest.setSource(request.getSource().getValue());
        googleRequest.setTarget(request.getTarget().getValue());
        googleRequest.setFormat(request.getFormat().getValue());

        String url = googleTranslateUrl + "?key=" + googleApiKey;
        return postTranslation(url, googleRequest);
    }

    @Override
    public String translateText(String textVi, Language targetLanguage) {
        if (textVi == null || textVi.trim().isEmpty()) {
            return "";
        }
        TranslationRequest translationRequest = new TranslationRequest();
        translationRequest.setQ(textVi);
        translationRequest.setSource(Language.VIETNAMESE);
        translationRequest.setTarget(targetLanguage);

        TranslationResponse translationResponse = translate(translationRequest);
        return translationResponse.getTranslatedText();
    }

    private TranslationResponse postTranslation(String url, GoogleTranslationRequest requestBody) {
        try {
            log.info("{} POST request with URL: {}", LOG_PREFIX, googleTranslateUrl + "?key=******");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<GoogleTranslationRequest> entity = new HttpEntity<>(requestBody, headers);

            log.info("{} POST request with payload: {}", LOG_PREFIX, objectMapper.writeValueAsString(requestBody));

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            log.info("{} POST response: code={}, body={}", LOG_PREFIX, response.getStatusCodeValue(), response.getBody());

            if (response.getBody() != null) {
                // Convert response body to GoogleTranslationResponse
                GoogleTranslationResponse googleResponse = objectMapper.readValue(response.getBody(), GoogleTranslationResponse.class);

                TranslationResponse translationResponse = new TranslationResponse();
                if (googleResponse != null &&
                        googleResponse.getData() != null &&
                        googleResponse.getData().getTranslations() != null &&
                        !googleResponse.getData().getTranslations().isEmpty()) {
                    translationResponse.setTranslatedText(googleResponse.getData().getTranslations().get(0).getTranslatedText());
                } else {
                    log.warn("{} Invalid response structure from Google Translate, returning default value.", LOG_PREFIX);
                    translationResponse.setTranslatedText(requestBody.getQ());
                }
                return translationResponse;
            }

            log.warn("{} Empty response body, returning default value.", LOG_PREFIX);
        } catch (Exception e) {
            log.error("{} POST request failed: {}", LOG_PREFIX, e.getMessage(), e);
        }

        // return default value
        TranslationResponse fallbackResponse = new TranslationResponse();
        fallbackResponse.setTranslatedText(requestBody.getQ());
        return fallbackResponse;
    }
}