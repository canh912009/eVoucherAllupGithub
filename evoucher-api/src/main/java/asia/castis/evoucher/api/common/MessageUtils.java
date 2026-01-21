package asia.castis.evoucher.api.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
public class MessageUtils {

    private static final String BASE_NAME = "messages";

    private MessageUtils() {
        throw new IllegalStateException("MessageUtils class");
    }

    public static String getMessage(String code, Locale locale) {
        return getMessage(code, locale, (Object) null);
    }

    public static String getMessage(String code, Locale locale, Object... args) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(BASE_NAME, locale);
        String message;
        try {
            message = resourceBundle.getString(code);
            message = MessageFormat.format(message, args);
        } catch (Exception ex) {
            log.info(">>> Can not get message with code {}", code);
            log.error(ex.getMessage(), ex);
            message = code;
        }

        return message;
    }

    public static String getMessage(String code) {
        return getMessage(code, LocaleContextHolder.getLocale(), (Object) null);
    }

    public static String getMessage(String code, Object... args) {
        return getMessage(code, LocaleContextHolder.getLocale(), args);
    }
}