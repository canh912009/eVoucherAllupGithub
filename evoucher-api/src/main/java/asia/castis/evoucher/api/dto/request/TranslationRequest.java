package asia.castis.evoucher.api.dto.request;

import asia.castis.evoucher.api.utils.Language;
import asia.castis.evoucher.api.utils.TranslationFormat;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TranslationRequest {
    private String q; // Text to translate (source: Vietnamese)
    private Language source = Language.VIETNAMESE;
    private Language target = Language.KOREAN;
    private TranslationFormat format = TranslationFormat.HTML;

}
