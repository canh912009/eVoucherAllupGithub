package asia.castis.evoucherservicefe.common.dto.publishreq.imcoming;

import asia.castis.evoucherservicefe.common.enums.EnumPublishType;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class RequestFromBE {
    private Long id;
    private String name;
    private EnumPublishType type;
    private Campaign campaign;
    private List<PublishDetail> publishDetails;
    private boolean isBooking;
    private String messageSubject;
    private String messageContent;
    private String messageCallingNumber;
    private Customer customer;
    private String bookingDate;
    private String publishDate;
    private String cancelDate;
    private boolean isTestSend;
    private boolean isReceiverNoDuplicateAllowed;
    private String smsType;
    private String templateId;
    private String publishStatusCode;
    private Integer voucherResendHistoryId;
    private String senderName;
}
