package asia.castis.evoucher.push.callback_receive.sms.model;
import asia.castis.evoucher.push.callback_receive.sms.common.CustomDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.ToString;
import org.joda.time.DateTime;

import java.util.List;

@Data
@ToString
public class Publish {
    private Long publishId;
    private Long campaignId;
    private String sendNm;
    private String bookingYn;

    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime bookingDt;
    private String testSendYn;
    private String receiverNoDuplAllowed;
    private String smsType;
    private String supplierId;
    private Double supplyDcCost;
    private Double supplyFeeRate;
    private String supplyVatIncYn;
    private String supplyCalculateMethod;
    private String customerId;
    private Double sellDcRate;
    private Double sellDcCost;
    private Double sellFeeRate;
    private String sellCalculateMethod;
    private Double sendCost;
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime sendDt;
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime cancelDt;
    private String sendStatusCd;
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime regDt;
    private String regId;
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime updtDt;
    private String updtId;
    private List<PublishDetail> publishDetails;

    private Campaign campaign;
}
