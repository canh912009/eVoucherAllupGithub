package asia.castis.evoucher.api.service.impl;

import asia.castis.evoucher.api.service.TranslationCacheService;
import asia.castis.evoucher.api.service.TranslationConnector;
import asia.castis.evoucher.api.service.TranslationService;
import asia.castis.evoucher.api.utils.Language;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class TranslationServiceImpl implements TranslationService {
    private final TranslationConnector translationConnector;
    private final TranslationCacheService cacheService;

    public TranslationServiceImpl(TranslationConnector translationConnector, TranslationCacheService cacheService) {
        this.translationConnector = translationConnector;
        this.cacheService = cacheService;
    }

    @Override
    public String translateText(String q, Language target) {
        if (q == null || q.trim().isEmpty()) {
            return "";
        }

        // Get target language: vi, en, ko...
        String language = target.getValue();
        String cachedTranslation = cacheService.getCachedTranslation(q, language);

        if (cachedTranslation != null) {
            log.info("Cache hit for {} in {}", q, language);
            return cachedTranslation;
        }

        // Request translation
        log.info("Request translation for {} in {}", q, language);
        String translatedText = translationConnector.getTranslatedText(q, target);

        // Escape HTML (ex: &quot; → " )
        translatedText = StringEscapeUtils.unescapeHtml4(translatedText);

        // Save to cache
        cacheService.saveTranslation(q, language, translatedText);

        return translatedText;
    }
}
