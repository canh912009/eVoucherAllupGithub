package asia.castis.evoucher.api.controller;

import asia.castis.evoucher.api.dto.response.ResponseData;
import asia.castis.evoucher.api.interceptor.LanguageContext;
import asia.castis.evoucher.api.service.TranslationService;
import asia.castis.evoucher.api.utils.Language;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/translation")
@Slf4j
public class TranslationController {

    private final TranslationService translationService;

    @Autowired
    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping("/translate")
    public ResponseData<String> translateText(@RequestBody Map<String, String> requestBody) {
        String textVi = requestBody.get("q");
        String targetLanguage = requestBody.get("target");
        log.info("Text vi: {}, target language: {}", textVi, targetLanguage);

        if (textVi == null || textVi.trim().isEmpty()) {
            return ResponseData.ok("");
        }

        // Convert target language to Language enum
        Language language = LanguageContext.convertToLanguageEnum(targetLanguage);

        // Request translation
        String translatedText;
        try {
            translatedText = translationService.translateText(textVi, language);
        } catch (Exception ex) {
            return ResponseData.failed(HttpStatus.INTERNAL_SERVER_ERROR, "Error during translation: " + ex.getMessage());
        }

        return ResponseData.ok(translatedText);
    }
}