package asia.castis.evoucher.api.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class GoogleTranslationResponse {
    private Data data;

    @lombok.Data
    public static class Data {
        private List<Translation> translations;
    }

    @lombok.Data
    public static class Translation {
        private String translatedText;
    }
}