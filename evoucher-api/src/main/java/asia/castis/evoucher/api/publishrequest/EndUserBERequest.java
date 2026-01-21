package asia.castis.evoucher.api.publishrequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EndUserBERequest {
    public EndUserBERequest(String userMobileNum, String userNm) {
        this.userMobileNum = userMobileNum;
        this.userNm = userNm;
    }

    // Encrypted
    private String userMobileNum;
    // NOT Encrypted
    private String userNm;
    private String gender;
    private String birthday;
    private String address;
}
