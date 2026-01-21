package asia.castis.evoucher.api.publishrequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class PublishTransferVoucher {
    private String oldEv;
    private String message;
    private EndUserBERequest newUser;

}
