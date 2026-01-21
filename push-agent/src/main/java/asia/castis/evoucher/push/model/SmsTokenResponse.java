package asia.castis.evoucher.push.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SmsTokenResponse {
    private String access_token;
    private int expires_in;
    private String token_type;
    private String scope;
}