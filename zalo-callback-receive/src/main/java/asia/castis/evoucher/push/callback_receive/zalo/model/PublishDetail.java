package asia.castis.evoucher.push.callback_receive.zalo.model;
import asia.castis.evoucher.push.callback_receive.zalo.common.CustomDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.ToString;
import org.joda.time.DateTime;

import java.math.BigInteger;

@Data
@ToString
public class PublishDetail {
    private Long publishDtlId;
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime regDt;
    private BigInteger publishId;
    private String receiverMobileNum;
    private String publishStatusCd;
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime creationDt;
    private String smsId;
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime smsSendDt;
    private String smsSendRsltCd;
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime smsSendRsltDt;
}
