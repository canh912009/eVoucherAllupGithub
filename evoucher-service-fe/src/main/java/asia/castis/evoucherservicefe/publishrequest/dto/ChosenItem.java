package asia.castis.evoucherservicefe.publishrequest.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChosenItem {
    Long goodsId;
    Integer quantity;
}

