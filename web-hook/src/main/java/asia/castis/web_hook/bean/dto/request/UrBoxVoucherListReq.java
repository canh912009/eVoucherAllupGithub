package asia.castis.web_hook.bean.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class UrBoxVoucherListReq extends UrBoxBaseRequest{
    String transaction_id;
}
