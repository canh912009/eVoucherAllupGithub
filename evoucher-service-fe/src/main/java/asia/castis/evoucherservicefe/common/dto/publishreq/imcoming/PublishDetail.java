package asia.castis.evoucherservicefe.common.dto.publishreq.imcoming;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PublishDetail {
    private Long id;
    private Long publishId;
    private String smsId;
    private String smsType;
    private Voucher voucher;

}
