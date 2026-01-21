package asia.castis.evoucher.api.service;

import asia.castis.evoucher.api.dto.request.TranslationRequest;
import asia.castis.evoucher.api.dto.response.TranslationResponse;
import asia.castis.evoucher.api.interceptor.LanguageContext;
import asia.castis.evoucher.api.utils.Language;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface TranslationConnector {
    Logger log = LoggerFactory.getLogger(TranslationConnector.class);

    TranslationResponse translate(TranslationRequest request);
    String translateText(String textVi, Language targetLanguage);

    default String getTranslatedText(String textVi, Language targetLanguage) {
        // Get language from context if not explicitly provided
        Language language = targetLanguage != null ? targetLanguage : LanguageContext.getLanguage();

        // No need to translate if target language is Vietnamese or if text is empty
        if (language == Language.VIETNAMESE || !StringUtils.hasText(textVi)) {
            return textVi;
        }

        log.info("Translating text '{}' to language {}", textVi, language);
        String translatedText = translateText(textVi, language);

        log.info("Language: {}, Translated text: '{}'", language.getValue(), translatedText);
        return translatedText;
    }
}