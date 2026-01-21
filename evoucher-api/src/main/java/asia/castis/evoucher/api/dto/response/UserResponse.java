package asia.castis.evoucher.api.dto.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserResponse {
    private String mobilePhone;
    private String name;
    private String gender;
    private String birthday;
    private String province;
    private String district;
    private String email;
}
