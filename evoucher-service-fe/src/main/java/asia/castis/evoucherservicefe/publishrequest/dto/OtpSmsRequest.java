package asia.castis.evoucherservicefe.publishrequest.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
public class OtpSmsRequest {
    private String phoneNumber;
    private String otp;
}
