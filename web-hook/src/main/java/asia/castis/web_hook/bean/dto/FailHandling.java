package asia.castis.web_hook.bean.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class FailHandling {
    String key;
    String type;
    String cause;
    String detail;
}
