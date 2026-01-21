package asia.castis.evoucher.api.interceptor;

import asia.castis.evoucher.api.utils.Language;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LanguageContext {
    private static final ThreadLocal<String> languageHolder = new ThreadLocal<>();

    private static final Map<String, Language> LANGUAGE_MAP = Arrays.stream(Language.values())
            .collect(Collectors.toMap(Language::getValue, lang -> lang));

    public static void setLanguage(String language) {
        languageHolder.set(language);
    }

    public static Language getLanguage() {
        String language = languageHolder.get();
        return LANGUAGE_MAP.getOrDefault(language, Language.VIETNAMESE); // Default to vietnamese
    }

    public static void clear() {
        languageHolder.remove();
    }

    /**
     * Converts a string to the corresponding Language enum.
     *
     * @param targetLanguage The input string (e.g., "vi", "en", etc.)
     * @return The corresponding Language enum value
     * @throws IllegalArgumentException if the string does not match any language
     */
    public static Language convertToLanguageEnum(String targetLanguage) {
        if (targetLanguage == null || targetLanguage.isEmpty())
            return getLanguage();
        Language language = LANGUAGE_MAP.get(targetLanguage);
        if (language == null) {
            throw new IllegalArgumentException("Invalid target language: " + targetLanguage);
        }
        return language;
    }
}
