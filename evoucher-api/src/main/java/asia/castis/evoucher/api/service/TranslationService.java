package asia.castis.evoucher.api.service;

import asia.castis.evoucher.api.utils.Language;

public interface TranslationService {
    String translateText(String q, Language target);
}
