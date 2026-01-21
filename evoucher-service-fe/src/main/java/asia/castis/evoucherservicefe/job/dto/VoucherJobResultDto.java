package asia.castis.evoucherservicefe.job.dto;

import asia.castis.evoucherservicefe.common.dto.QueueMessage;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class VoucherJobResultDto implements QueueMessage {
    private List<String> voucherIds;
    private String requestDate;
    private String requestType;
}
