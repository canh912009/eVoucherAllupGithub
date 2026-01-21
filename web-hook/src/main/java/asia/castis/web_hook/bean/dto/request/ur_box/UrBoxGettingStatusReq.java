package asia.castis.web_hook.bean.dto.request.ur_box;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UrBoxGettingStatusReq {
    List<String> transactionsId;
}
