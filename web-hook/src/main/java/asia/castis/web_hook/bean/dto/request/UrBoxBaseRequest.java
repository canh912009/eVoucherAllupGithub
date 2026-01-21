package asia.castis.web_hook.bean.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PUBLIC)
@Accessors(chain = true)
public class UrBoxBaseRequest {
    String app_secret;
    Integer app_id;
}
