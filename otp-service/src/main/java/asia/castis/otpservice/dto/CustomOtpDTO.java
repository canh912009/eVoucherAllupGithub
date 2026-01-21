package asia.castis.otpservice.dto;

import asia.castis.otpservice.common.EnumOtpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class CustomOtpDTO {
    private String otp;
    private String uuid;
    private EnumOtpStatus status;
    private Integer counter;
    private Long regDt;
    private String expireDtStr;
}
