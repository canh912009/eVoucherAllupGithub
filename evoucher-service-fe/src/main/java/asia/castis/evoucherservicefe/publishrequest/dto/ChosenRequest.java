package asia.castis.evoucherservicefe.publishrequest.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChosenRequest {
    String parentVoucherId;
    String token;
    List<ChosenItem> products;
    String type; // BULK/CHOICE
}
