package asia.castis.evoucherservicefe.common.dto.createmessageresult;

import asia.castis.evoucherservicefe.common.enums.EnumPublishDetailStatus;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailResult {
    private String messageId;
    private Long publishDetailId;
    private String smsType;
    private String phoneNumber;
    private String email;
    private EnumPublishDetailStatus result;
    private String message;
    private String receivedTime;
    private String sendDatetime;
    private String sendResultDatetime;
}
