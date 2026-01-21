package asia.castis.evoucherservicefe.storerequest.dto;

import asia.castis.evoucherservicefe.common.enums.EnumAction;
import lombok.Data;

@Data
public class StoreRequest {
    private EnumAction action;
    private Store store;
}
