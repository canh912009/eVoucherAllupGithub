package asia.castis.evoucherservicefe.common.dto.publishreq.imcoming;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Supplier {
    private String id;
    private String name;
    private String description;
    private String imgUrl;
}
