package asia.castis.evoucher.api.service;

import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class TranslationCacheService {
    private final RMapCache<String, Map<String, String>> translationCache;

    public TranslationCacheService(@Qualifier("redClient") RedissonClient redissonClient) {
        this.translationCache = redissonClient.getMapCache("translation_cache");
    }

    /**
     * Hash input text to generate a unique key
     */
    private String hashText(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    /**
     * Get translation from cache
     */
    public String getCachedTranslation(String text, String language) {
        String key = "translation:" + hashText(text);
        Map<String, String> translations = translationCache.get(key);
        return translations != null ? translations.get(language) : null;
    }

    /**
     * Save translation to cache
     */
    public void saveTranslation(String text, String language, String translatedText) {
        String key = "translation:" + hashText(text);

        Map<String, String> translations = translationCache.get(key);
        if (translations == null) {
            translations = new HashMap<>();
        }
        translations.put(language, translatedText);

        // Cache for 30 days
        translationCache.put(key, translations, 30, TimeUnit.DAYS);
    }

}