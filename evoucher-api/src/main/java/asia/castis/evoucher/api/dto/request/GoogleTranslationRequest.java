package asia.castis.evoucher.api.dto.request;

import lombok.Data;

@Data
public class GoogleTranslationRequest {
    private String q;
    private String source;
    private String target;
    private String format;
}
